package com.wannacry.stockup.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.db.entity.CategoryEntity
import com.wannacry.stockup.domain.db.entity.ItemEntity
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.util.UUID

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name")
    fun getAll(): Flow<List<CategoryEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)


    @Delete
    suspend fun delete(category: CategoryEntity)
}


@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY name")
    fun getAll(): Flow<List<ItemEntity>>


    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): ItemEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)


    @Update
    suspend fun update(item: ItemEntity)


    @RequiresApi(Build.VERSION_CODES.O)
    @Query("UPDATE items SET quantity = :quantity, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateQuantity(id: UUID, quantity: Double, updatedAt: Instant = Instant.now())


    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: UUID)


    @Query("SELECT * FROM items WHERE stockIndicator = :indicator")
    fun getByIndicator(indicator: StockIndicator): Flow<List<ItemEntity>>


    @Query("SELECT * FROM items WHERE expiryDate IS NOT NULL AND expiryDate <= :date ORDER BY expiryDate")
    fun getExpiringOnOrBefore(date: String): Flow<List<ItemEntity>>
}


@Dao
interface StockHistoryDao {
    @Query("SELECT * FROM stock_history WHERE itemId = :itemId ORDER BY createdAt DESC")
    fun getForItem(itemId: UUID): Flow<List<StockHistoryEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: StockHistoryEntity)
}