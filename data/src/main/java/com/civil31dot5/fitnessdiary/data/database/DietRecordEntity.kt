package com.civil31dot5.fitnessdiary.data.database

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "diet_record")
data class DietRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "datetime")
    val dateTime: LocalDateTime,
    @ColumnInfo(name = "note")
    val note: String
)

data class DietRecordWithImages(
    @Embedded val dietRecord: DietRecordEntity,
    @Relation(
       parentColumn = "id",
       entityColumn = "record_id"
    )
    val images: List<RecordImageEntity>
)