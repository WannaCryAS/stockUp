package com.wannacry.stockup.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.usecase.CategoryUseCase
import com.wannacry.stockup.domain.usecase.StockUpUseCase
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class SettingViewModel(
    stockUpUseCase: StockUpUseCase,
    private val categoryUseCase: CategoryUseCase
): BaseStockUpViewModel(stockUpUseCase, categoryUseCase) {

    fun addCategory(name: String) {
        viewModelScope.launch {
            val category = Category(name = name)
            categoryUseCase.addCategory(category)
        }
    }

    fun deleteCategory(id: UUID) {
        viewModelScope.launch {
            categoryUseCase.deleteCategory(id)
        }
    }
}