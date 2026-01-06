package com.wannacry.stockup.domain.repo.category

import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.domain.dao.CategoryDao
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.mapper.toDomain
import com.wannacry.stockup.domain.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
): CategoryRepository {

    override fun getCategories(): Flow<List<Category>> =
        categoryDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addCategory(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun deleteCategory(id: UUID) {
        categoryDao.deleteById(id)
    }

}