package com.civil31dot5.fitnessdiary.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "strava_sport")
data class StravaSportEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "datetime")
    val datetime: LocalDateTime,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "distance")
    val distance: Double,
    @ColumnInfo(name = "calories")
    val calories: Double,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "elapsed_time_sec")
    val elapsedTimeSec: Long
)
