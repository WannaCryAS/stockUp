package com.wannacry.stockup.domain.repo.stockUp

import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.StockHistory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

interface StockUpRepository {

    //ITEM
    fun getItems(): Flow<List<Item>>
    suspend fun getItem(id: UUID): Item?
    suspend fun addItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(id: UUID)
    suspend fun getExpiringOnOrBefore(date: LocalDate): List<Item>
    suspend fun markExpired(id: UUID)

    //HISTORY
    fun getHistory(itemId: UUID): Flow<List<StockHistory>>
}