package com.wannacry.stockup.domain.usecase

import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.domain.db.entity.TaskItemEntity
import com.wannacry.stockup.domain.repo.task.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TaskUseCase(private val repository: TaskRepository) {

    // TASK
    fun getTasks(): Flow<List<Task>> = repository.getTasks()

    suspend fun addTask(task: Task) {
        repository.addTask(task)
    }

    suspend fun deleteTask(id: UUID) {
        repository.deleteTask(id)
    }

    fun getTaskById(id: UUID): Flow<Task?> = repository.getTaskById(id)

    // TASK ITEMS
    fun getItemsForTask(taskId: UUID): Flow<List<Item>> = repository.getItemsForTask(taskId)

    suspend fun getTaskItemsWithQuantities(taskId: UUID): List<TaskItemEntity> = repository.getTaskItemsWithQuantities(taskId)

    suspend fun addItemToTask(taskId: UUID, itemId: UUID, requiredQuantity: Double) {
        repository.addItemToTask(taskId, itemId, requiredQuantity)
    }

    suspend fun removeItemFromTask(taskId: UUID, itemId: UUID) {
        repository.removeItemFromTask(taskId, itemId)
    }
}