package com.wannacry.stockup.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wannacry.stockup.domain.db.entity.ItemEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAll(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: UUID): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity)

    @Update
    suspend fun update(item: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("SELECT * FROM items WHERE expiryDate <= :date")
    suspend fun getExpiringOnOrBeforeSuspend(date: LocalDate): List<ItemEntity>
}