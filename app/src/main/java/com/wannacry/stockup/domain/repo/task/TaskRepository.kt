package com.wannacry.stockup.domain.repo.task

import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.domain.db.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TaskRepository {

    fun getTasks(): Flow<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun deleteTask(id: UUID)

    fun getTaskById(id: UUID): Flow<Task?>

    // TASK ITEMS
    fun getItemsForTask(taskId: UUID): Flow<List<Item>>

    suspend fun getTaskItemsWithQuantities(taskId: UUID): List<TaskItemEntity>

    suspend fun addItemToTask(taskId: UUID, itemId: UUID, requiredQuantity: Double)

    suspend fun removeItemFromTask(taskId: UUID, itemId: UUID)

    fun getTaskItemCount(taskId: UUID): Flow<Int>
}