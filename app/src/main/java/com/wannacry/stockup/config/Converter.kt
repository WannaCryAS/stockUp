package com.wannacry.stockup.config

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.util.UUID


class StockUpConverters {
    @TypeConverter
    fun fromUUID(u: UUID?): String? = u?.toString()


    @TypeConverter
    fun toUUID(s: String?): UUID? = s?.let { UUID.fromString(it) }


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromInstant(i: Instant?): Long? = i?.toEpochMilli()


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toInstant(l: Long?): Instant? = l?.let { Instant.ofEpochMilli(it) }


    @TypeConverter
    fun fromLocalDate(d: LocalDate?): String? = d?.toString()


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(s: String?): LocalDate? = s?.let { LocalDate.parse(it) }


    @TypeConverter
    fun stockIndicatorToString(ind: StockIndicator?): String? = ind?.name


    @TypeConverter
    fun stringToStockIndicator(s: String?): StockIndicator? = s?.let { StockIndicator.valueOf(it) }


    @TypeConverter
    fun actionTypeToString(a: ActionType?): String? = a?.name


    @TypeConverter
    fun stringToActionType(s: String?): ActionType? = s?.let { ActionType.valueOf(it) }
}