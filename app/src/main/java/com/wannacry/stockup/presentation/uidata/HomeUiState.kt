package com.wannacry.stockup.presentation.uidata

import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.Item

data class HomeUiState(
    val items: List<Item> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedStockFilter: String? = null, // "LOW" atau "EMPTY"
    val searchQuery: String = "",
    val isLoading: Boolean = true
)