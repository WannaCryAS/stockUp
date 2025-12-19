package com.wannacry.stockup.domain.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.dao.CategoryDao
import com.wannacry.stockup.domain.dao.ItemDao
import com.wannacry.stockup.domain.dao.StockHistoryDao
import com.wannacry.stockup.domain.data.ActionType
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.StockHistory
import com.wannacry.stockup.domain.data.mapper.toDomain
import com.wannacry.stockup.domain.data.mapper.toEntity
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class StockUpRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val itemDao: ItemDao,
    private val historyDao: StockHistoryDao
) : StockUpRepository {

    // CATEGORY
    override fun getCategories(): Flow<List<Category>> =
        categoryDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun deleteCategory(id: UUID) {
        categoryDao.deleteById(id)
    }

    // ITEMS
    override fun getItems(): Flow<List<Item>> =
        itemDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getItem(id: UUID): Item? =
        itemDao.getById(id)?.toDomain()

    override suspend fun addItem(item: Item) {
        val entity = item.toEntity()
        itemDao.insert(entity)
        historyDao.insert(
            StockHistoryEntity(
                itemId = entity.id,
                actionType = ActionType.ADD,
                description = "Item added",
                quantityChange = entity.quantity,
                createdAt = Instant.now()
            )
        )
    }

    override suspend fun updateItem(item: Item) {
        val oldItemEntity = itemDao.getById(item.id) ?: return
        val newItemEntity = item.toEntity().copy(updatedAt = Instant.now())
        itemDao.update(newItemEntity)

        if (oldItemEntity.quantity != newItemEntity.quantity) {
            historyDao.insert(
                StockHistoryEntity(
                    itemId = newItemEntity.id,
                    actionType = ActionType.UPDATE,
                    description = "Kuantiti dikemaskini daripada ${oldItemEntity.quantity} kepada ${newItemEntity.quantity}",
                    quantityChange = newItemEntity.quantity - oldItemEntity.quantity,
                    createdAt = Instant.now()
                )
            )
        }
    }

    override suspend fun deleteItem(id: UUID) {
        val old = itemDao.getById(id)
        itemDao.deleteById(id)
        historyDao.insert(
            StockHistoryEntity(
                itemId = id,
                actionType = ActionType.REMOVE,
                description = "Item deleted",
                quantityChange = -(old?.quantity ?: 0.0),
                createdAt = Instant.now()
            )
        )
    }

    override suspend fun getExpiringOnOrBefore(date: LocalDate): List<Item> =
        itemDao.getExpiringOnOrBeforeSuspend(date).map { it.toDomain() }

    override suspend fun markExpired(id: UUID) {
        val item = itemDao.getById(id) ?: return
        val updated = item.copy(
            stockIndicator = StockIndicator.EXPIRED.name,
            updatedAt = Instant.now()
        )
        itemDao.update(updated)
    }

    // HISTORY
    override fun getHistory(itemId: UUID): Flow<List<StockHistory>> =
        historyDao.getHistory(itemId).map { list -> list.map { it.toDomain() } }
}