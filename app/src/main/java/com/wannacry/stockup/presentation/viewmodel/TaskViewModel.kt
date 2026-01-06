package com.wannacry.stockup.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.domain.usecase.CategoryUseCase
import com.wannacry.stockup.domain.usecase.StockUpUseCase
import com.wannacry.stockup.domain.usecase.TaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel(
    private val useCase: TaskUseCase,
    private val stockUpUseCase: StockUpUseCase,
    categoryUseCase: CategoryUseCase
): BaseStockUpViewModel(stockUpUseCase, categoryUseCase) {

    // Task Detail States
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _taskItems = MutableStateFlow<List<Item>>(emptyList())
    val taskItems: StateFlow<List<Item>> = _taskItems.asStateFlow()

    val tasks: StateFlow<List<Task>> = useCase.getTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Task Operations
    fun loadTaskDetail(taskId: UUID) {
        viewModelScope.launch {
            useCase.getTaskById(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
        viewModelScope.launch {
            useCase.getItemsForTask(taskId).collect { items ->
                _taskItems.value = items
            }
        }
    }

    fun addTask(title: String, description: String? = null, items: List<Pair<UUID, Double>> = emptyList()) {
        viewModelScope.launch {
            val task = Task(title = title, description = description)
            useCase.addTask(task)
            items.forEach { (itemId, qty) ->
                useCase.addItemToTask(task.id, itemId, qty)
            }
        }
    }

    fun deleteTask(id: UUID) {
        viewModelScope.launch {
            useCase.deleteTask(id)
        }
    }

    fun addItemToTask(taskId: UUID, itemId: UUID, qty: Double) {
        viewModelScope.launch {
            useCase.addItemToTask(taskId, itemId, qty)
        }
    }

    fun removeItemFromTask(taskId: UUID, itemId: UUID) {
        viewModelScope.launch {
            useCase.removeItemFromTask(taskId, itemId)
        }
    }

    fun executeTaskStep(taskId: UUID) {
        viewModelScope.launch {
            val taskMaterials = useCase.getTaskItemsWithQuantities(taskId)
            taskMaterials.forEach { taskItem ->
                val currentItem = stockUpUseCase.getItemById(taskItem.itemId)
                if (currentItem != null) {
                    val newQuantity = (currentItem.quantity - taskItem.requiredQuantity).coerceAtLeast(0.0)
                    val updatedItem = currentItem.copy(
                        quantity = newQuantity,
                        stockIndicator = stockIndicator(newQuantity.toString(), currentItem.minLimit.toString(), currentItem.expiryDate)
                    )
                    stockUpUseCase.updateItem(updatedItem)
                }
            }
        }
    }
}