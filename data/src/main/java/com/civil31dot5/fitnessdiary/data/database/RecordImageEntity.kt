package com.civil31dot5.fitnessdiary.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_image")
data class RecordImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "record_id")
    val recordId: String,
    @ColumnInfo(name = "file_path")
    val filePath: String,
    @ColumnInfo(name = "note")
    val note: String
)
