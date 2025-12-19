package com.wannacry.stockup.domain.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
data class Category(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val createdAt: Instant = Instant.now()
)