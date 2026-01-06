package com.wannacry.stockup.domain.usecase

import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.StockHistory
import com.wannacry.stockup.domain.repo.stockUp.StockUpRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class StockUpUseCase(private val repository: StockUpRepository) {

    fun getAllItems(): Flow<List<Item>> = repository.getItems()

    suspend fun getItemById(id: UUID): Item? = repository.getItem(id)

    suspend fun addItem(item: Item) {
        repository.addItem(item)
    }

    suspend fun updateItem(item: Item) {
        repository.updateItem(item)
    }

    suspend fun deleteItem(id: UUID) {
        repository.deleteItem(id)
    }

    fun getHistory(itemId: UUID): Flow<List<StockHistory>> = repository.getHistory(itemId)
}
