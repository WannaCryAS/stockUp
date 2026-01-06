package com.wannacry.stockup.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wannacry.stockup.domain.db.entity.ItemEntity
import com.wannacry.stockup.domain.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    fun getTaskById(id: UUID): Flow<TaskEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: UUID)

    @Query("""
        SELECT items.* FROM items 
        INNER JOIN task_items ON items.id = task_items.itemId 
        WHERE task_items.taskId = :taskId
    """)
    fun getItemsForTask(taskId: UUID): Flow<List<ItemEntity>>
}