package com.wannacry.stockup.domain.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "stock_history")
@RequiresApi(Build.VERSION_CODES.O)
data class StockHistoryEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val itemId: UUID,
    val actionType: String,
    val description: String?,
    val quantityChange: Double?,
    val createdAt: Instant = Instant.now()
)