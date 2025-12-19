package com.wannacry.stockup.domain.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "categories")
@RequiresApi(Build.VERSION_CODES.O)
data class CategoryEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val createdAt: Instant = Instant.now()
)