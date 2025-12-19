package com.wannacry.stockup.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.config.ActionType
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.dao.CategoryDao
import com.wannacry.stockup.domain.db.entity.CategoryEntity
import com.wannacry.stockup.domain.db.entity.ItemEntity
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * StockUpRepository - single source of truth for local DB operations.
 *
 * Note:
 * - DAOs are provided by DI (Koin) or manual construction.
 * - All mutating operations also insert a StockHistoryEntity record.
 */
open class PantryRepository(
    private val itemDao: ItemDao,
    private val categoryDao: CategoryDao,
    private val historyDao: StockHistoryDao
) {

    // -----------------------
    // Categories
    // -----------------------
    fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.getAll()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addCategory(name: String) {
        val entity = CategoryEntity(name = name)
        categoryDao.insert(entity)
    }

    suspend fun deleteCategory(entity: CategoryEntity) {
        categoryDao.delete(entity)
    }

    // -----------------------
    // Items (current state)
    // -----------------------
    fun observeItems(): Flow<List<ItemEntity>> = itemDao.getAll()

    suspend fun getItem(itemId: UUID): ItemEntity? = itemDao.getById(itemId)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addItem(
        name: String,
        description: String? = null,
        quantity: Double,
        unit: String,
        expiryDate: LocalDate? = null,
        categoryId: UUID? = null,
        minLimit: Double = 0.0
    ) {
        val item = ItemEntity(
            name = name,
            description = description,
            quantity = quantity,
            unit = unit,
            expiryDate = expiryDate,
            categoryId = categoryId,
            minLimit = minLimit,
            stockIndicator = computeIndicator(quantity, minLimit),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        itemDao.insert(item)

        historyDao.insert(
            StockHistoryEntity(
                itemId = item.id,
                actionType = ActionType.ADD.name,
                description = "Added item",
                quantityChange = quantity,
                createdAt = Instant.now()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateItem(updated: ItemEntity) {
        val newUpdated = updated.copy(updatedAt = Instant.now())
        itemDao.update(newUpdated)

        historyDao.insert(
            StockHistoryEntity(
                itemId = newUpdated.id,
                actionType = ActionType.UPDATE.name,
                description = "Updated item metadata",
                quantityChange = null,
                createdAt = Instant.now()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateQuantity(itemId: UUID, newQuantity: Double) {
        val existing = itemDao.getById(itemId) ?: return
        val indicator = computeIndicator(newQuantity, existing.minLimit)
        val updated = existing.copy(quantity = newQuantity, stockIndicator = indicator, updatedAt = Instant.now())
        itemDao.update(updated)

        val change = newQuantity - existing.quantity
        val action = when {
            newQuantity <= 0.0 -> ActionType.UPDATE.name
            change > 0 -> ActionType.ADD.name
            else -> ActionType.REMOVE.name
        }

        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = action,
                description = "Quantity updated",
                quantityChange = change,
                createdAt = Instant.now()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteItem(itemId: UUID) {
        val item = itemDao.getById(itemId) ?: return
        itemDao.deleteById(itemId)

        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = ActionType.REMOVE.name,
                description = "Item deleted",
                quantityChange = -item.quantity,
                createdAt = Instant.now()
            )
        )
    }

    // -----------------------
    // Convenience actions
    // -----------------------
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun consumeItem(itemId: UUID, amount: Double) {
        if (amount <= 0.0) return
        val item = itemDao.getById(itemId) ?: return
        val newQty = (item.quantity - amount).coerceAtLeast(0.0)
        val indicator = computeIndicator(newQty, item.minLimit)
        val updated = item.copy(quantity = newQty, stockIndicator = indicator, updatedAt = Instant.now())
        itemDao.update(updated)

        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = ActionType.CONSUME.name,
                description = "Consumed $amount ${item.unit}",
                quantityChange = -amount,
                createdAt = Instant.now()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun restockItem(itemId: UUID, amount: Double) {
        if (amount <= 0.0) return
        val item = itemDao.getById(itemId) ?: return
        val newQty = item.quantity + amount
        val indicator = computeIndicator(newQty, item.minLimit)
        val updated = item.copy(quantity = newQty, stockIndicator = indicator, updatedAt = Instant.now())
        itemDao.update(updated)

        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = ActionType.ADD.name,
                description = "Restocked $amount ${item.unit}",
                quantityChange = amount,
                createdAt = Instant.now()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun markItemEmpty(itemId: UUID) {
        val item = itemDao.getById(itemId) ?: return
        val prevQty = item.quantity
        val updated = item.copy(quantity = 0.0, stockIndicator = StockIndicator.EMPTY.name, updatedAt = Instant.now())
        itemDao.update(updated)

        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = ActionType.UPDATE.name,
                description = "Marked as empty",
                quantityChange = -prevQty,
                createdAt = Instant.now()
            )
        )
    }

    // -----------------------
    // Observers / queries
    // -----------------------
    fun observeLowStock(): Flow<List<ItemEntity>> = itemDao.getByIndicator(StockIndicator.LOW)

    fun observeEmptyStock(): Flow<List<ItemEntity>> = itemDao.getByIndicator(StockIndicator.EMPTY)

    fun observeAvailableStock(): Flow<List<ItemEntity>> = itemDao.getByIndicator(StockIndicator.AVAILABLE)

    /**
     * Returns items expiring on or before given date (LocalDate.toString() format).
     * The DAO implementation in the module used Flow; if you need a suspend list version,
     * you can add it to ItemDao. Here we offer Flow-based mapping for convenience.
     */
    fun observeExpiringOnOrBefore(date: LocalDate): Flow<List<ItemEntity>> =
        itemDao.getExpiringOnOrBefore(date.toString())

    fun observeHistoryForItem(itemId: UUID): Flow<List<StockHistoryEntity>> =
        historyDao.getForItem(itemId)

    // -----------------------
    // Helper
    // -----------------------
    private fun computeIndicator(quantity: Double, minLimit: Double): String {
        return when {
            quantity <= 0.0 -> StockIndicator.EMPTY.name
            quantity <= minLimit -> StockIndicator.LOW.name
            else -> StockIndicator.AVAILABLE.name
        }
    }

    // -----------------------
    // Worker friendly helpers (suspend variants)
    // -----------------------
    /**
     * Helper to find expiring items as a List (suspend). Uses DAO Flow.first() if DAO returns Flow.
     * If your ItemDao already provides a suspend function that returns List<ItemEntity>, prefer that.
     */
    suspend fun getExpiringOnOrBeforeSuspend(date: LocalDate): List<ItemEntity> {
        // itemDao.getExpiringOnOrBefore returns Flow<List<ItemEntity>> in module above.
        return itemDao.getExpiringOnOrBefore(date.toString()).first()
    }

    /**
     * Update item indicator to EXPIRED and add history for expiry.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun markItemExpired(itemId: UUID) {
        val item = itemDao.getById(itemId) ?: return
        val updated = item.copy(stockIndicator = StockIndicator.EXPIRED.name, updatedAt = Instant.now())
        itemDao.update(updated)
        historyDao.insert(
            StockHistoryEntity(
                itemId = itemId,
                actionType = ActionType.EXPIRE.name,
                description = "Marked expired",
                quantityChange = null,
                createdAt = Instant.now()
            )
        )
    }
}