package com.wannacry.stockup.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.usecase.StockUpUseCase
import com.wannacry.stockup.presentation.uidata.HomeUiState
import com.wannacry.stockup.presentation.uidata.ItemDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class StockUpViewModel(private val useCase: StockUpUseCase) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _selectedStockFilter = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _itemDetailUiState = MutableStateFlow(ItemDetailUiState())
    val itemDetailUiState: StateFlow<ItemDetailUiState> = _itemDetailUiState.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        useCase.getAllItems(),
        useCase.getAllCategories(),
        _selectedCategory,
        _selectedStockFilter,
        _searchQuery,
    ) { items, categories, selectedCategory, selectedStockFilter, searchQuery ->
        var filteredItems = items

        // Filter by Category
        if (selectedCategory != null) {
            filteredItems = filteredItems.filter { it.categoryId == selectedCategory.id }
        }

        // Filter by Stock Status
        if (selectedStockFilter != null) {
            filteredItems = filteredItems.filter { it.stockIndicator == selectedStockFilter }
        }

        // Filter by Search Query
        if (searchQuery.isNotBlank()) {
            filteredItems = filteredItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        HomeUiState(
            items = filteredItems,
            categories = categories,
            selectedCategory = selectedCategory,
            selectedStockFilter = selectedStockFilter,
            searchQuery = searchQuery,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun selectStockFilter(filter: String?) {
        _selectedStockFilter.value = filter
    }

    fun getItemDetails(itemId: UUID) {
        viewModelScope.launch {
            _itemDetailUiState.value = ItemDetailUiState(isLoading = true)
            val item = useCase.getItemById(itemId)
            if (item != null) {
                val history = useCase.getHistory(itemId).first()
                _itemDetailUiState.value =
                    ItemDetailUiState(item = item, history = history, isLoading = false)
            } else {
                _itemDetailUiState.value = ItemDetailUiState(isLoading = false) // Item not found
            }
        }
    }

    fun addItem(
        name: String,
        quantity: String,
        unit: String,
        limit: String,
        expiryDate: LocalDate?,
        categoryId: UUID?,
        stockIndicator: String
    ) {
        viewModelScope.launch {
            val item = Item(
                name = name,
                quantity = quantity.toDoubleOrNull() ?: 0.0,
                unit = unit,
                minLimit = limit.toDoubleOrNull() ?: 0.0,
                expiryDate = expiryDate,
                categoryId = categoryId,
                stockIndicator = stockIndicator
            )
            useCase.addItem(item)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            useCase.updateItem(item)
        }
    }

    fun deleteItem(id: UUID) {
        viewModelScope.launch {
            useCase.deleteItem(id)
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val category = Category(name = name)
            useCase.addCategory(category)
        }
    }

    fun deleteCategory(id: UUID) {
        viewModelScope.launch {
            useCase.deleteCategory(id)
        }
    }

    fun stockIndicator(quantity: String, minLimit: String, expiryDate: LocalDate? = null): String {
        val q = quantity.toDoubleOrNull() ?: 0.0
        val m = minLimit.toDoubleOrNull() ?: 0.0
        val today = LocalDate.now()

        return when {
            expiryDate != null && expiryDate.isBefore(today) -> StockIndicator.EXPIRED.name
            (q <= m) && (q <= 0.0) -> StockIndicator.EMPTY.name
            q <= m -> StockIndicator.LOW.name
            else -> StockIndicator.AVAILABLE.name
        }
    }
}