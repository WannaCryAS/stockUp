package com.wannacry.stockup.domain.repo.category

import com.wannacry.stockup.domain.data.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {

    fun getCategories(): Flow<List<Category>>
    suspend fun addCategory(category: Category)
    suspend fun deleteCategory(id: UUID)
}