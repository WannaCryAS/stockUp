package com.wannacry.stockup.domain.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.UUID

object ActionType {
    const val ADD = "ADD"
    const val UPDATE = "UPDATE"
    const val REMOVE = "REMOVE"
}

@RequiresApi(Build.VERSION_CODES.O)
data class StockHistory(
    val id: UUID = UUID.randomUUID(),
    val itemId: UUID,
    val actionType: String,
    val description: String? = null,
    val quantityChange: Double? = null,
    val createdAt: Instant = Instant.now()
)