package com.wannacry.stockup.domain.repo.task

import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.domain.dao.TaskDao
import com.wannacry.stockup.domain.dao.TaskItemDao
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.domain.data.mapper.toDomain
import com.wannacry.stockup.domain.data.mapper.toEntity
import com.wannacry.stockup.domain.db.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val taskItemDao: TaskItemDao
): TaskRepository {

    // TASK
    override fun getTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { list -> list.map { entity -> entity.toDomain() } }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun deleteTask(id: UUID) {
        taskDao.deleteTaskById(id)
    }

    override fun getTaskById(id: UUID): Flow<Task?> =
        taskDao.getTaskById(id).map { entity -> entity?.toDomain() }

    // TASK ITEMS
    override fun getItemsForTask(taskId: UUID): Flow<List<Item>> =
        taskDao.getItemsForTask(taskId).map { list -> list.map { entity -> entity.toDomain() } }

    override suspend fun getTaskItemsWithQuantities(taskId: UUID): List<TaskItemEntity> =
        taskItemDao.getTaskItemsByTaskId(taskId)

    override suspend fun addItemToTask(taskId: UUID, itemId: UUID, requiredQuantity: Double) {
        taskItemDao.insertTaskItem(TaskItemEntity(taskId, itemId, requiredQuantity))
    }

    override suspend fun removeItemFromTask(taskId: UUID, itemId: UUID) {
        taskItemDao.deleteTaskItem(taskId, itemId)
    }

    override fun getTaskItemCount(taskId: UUID): Flow<Int> =
        taskItemDao.getItemCountForTask(taskId)
}