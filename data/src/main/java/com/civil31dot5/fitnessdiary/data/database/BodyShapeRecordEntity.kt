package com.civil31dot5.fitnessdiary.data.database

import androidx.room.*
import java.time.LocalDateTime

@Entity(tableName = "body_shape_record")
data class BodyShapeRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "datetime")
    val dateTime: LocalDateTime,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "weight")
    val weight: Double,
    @ColumnInfo(name = "body_fat_percentage")
    val bodyFatPercentage: Double? = null,
)

data class BodyShapeRecordWithImages(
    @Embedded val bodyShapeRecord: BodyShapeRecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "record_id"
    )
    val images: List<RecordImageEntity>
)

