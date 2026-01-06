package com.wannacry.stockup.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wannacry.stockup.domain.db.entity.TaskItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItemEntity)

    @Query("SELECT COUNT(*) FROM task_items WHERE taskId = :taskId")
    fun getItemCountForTask(taskId: UUID): Flow<Int>

    @Query("SELECT * FROM task_items WHERE taskId = :taskId")
    suspend fun getTaskItemsByTaskId(taskId: UUID): List<TaskItemEntity>

    @Query("DELETE FROM task_items WHERE taskId = :taskId AND itemId = :itemId")
    suspend fun deleteTaskItem(taskId: UUID, itemId: UUID)
}