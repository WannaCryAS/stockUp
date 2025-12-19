package com.wannacry.stockup.config.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant?): String? = instant?.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toInstant(value: String?): Instant? = value?.let { Instant.parse(it) }
}