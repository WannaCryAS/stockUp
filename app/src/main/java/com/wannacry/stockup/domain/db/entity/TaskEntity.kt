package com.wannacry.stockup.domain.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "tasks")
@RequiresApi(Build.VERSION_CODES.O)
data class TaskEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val createdAt: Instant = Instant.now()
)