package com.wannacry.stockup.domain.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
data class Item(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val quantity: Double,
    val unit: String,
    val expiryDate: LocalDate? = null,
    val categoryId: UUID? = null,
    val stockIndicator: String,
    val minLimit: Double = 1.0,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)