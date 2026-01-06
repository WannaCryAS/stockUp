package com.wannacry.stockup.domain.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val createdAt: Instant = Instant.now()
)