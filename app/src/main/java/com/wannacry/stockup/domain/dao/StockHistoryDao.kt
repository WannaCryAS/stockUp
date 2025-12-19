package com.wannacry.stockup.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface StockHistoryDao {

    @Query("SELECT * FROM stock_history WHERE itemId = :itemId ORDER BY createdAt DESC")
    fun getHistory(itemId: UUID): Flow<List<StockHistoryEntity>>

    @Insert
    suspend fun insert(history: StockHistoryEntity)
}