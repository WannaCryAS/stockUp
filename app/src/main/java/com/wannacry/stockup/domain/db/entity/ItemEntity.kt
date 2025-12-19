package com.wannacry.stockup.domain.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "items")
@RequiresApi(Build.VERSION_CODES.O)
data class ItemEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String?,
    val quantity: Double,
    val unit: String,
    val expiryDate: LocalDate?,
    val categoryId: UUID?,
    val stockIndicator: String,
    val minLimit: Double,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)