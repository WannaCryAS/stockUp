package com.wannacry.stockup.domain.usecase

import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.repo.category.CategoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CategoryUseCase(private val repository: CategoryRepository) {

    fun getAllCategories(): Flow<List<Category>> = repository.getCategories()

    suspend fun addCategory(category: Category) {
        repository.addCategory(category)
    }

    suspend fun deleteCategory(id: UUID) {
        repository.deleteCategory(id)
    }
}