package com.wannacry.stockup.presentation.uidata

import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.StockHistory

data class ItemDetailUiState(
    val item: Item? = null,
    val history: List<StockHistory> = emptyList(),
    val isLoading: Boolean = true
)