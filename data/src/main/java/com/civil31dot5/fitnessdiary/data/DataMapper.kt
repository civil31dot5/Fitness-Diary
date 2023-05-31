package com.civil31dot5.fitnessdiary.data

import com.civil31dot5.fitnessdiary.data.database.*
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord


fun DietRecord.toDietRecordEntity(): DietRecordEntity {
    return DietRecordEntity(id, name, dateTime, note)
}

fun RecordImage.toRecordImageEntity(recordId: String, newFilePath: String): RecordImageEntity {
    return RecordImageEntity(id, recordId, newFilePath, note)
}

fun DietRecordWithImages.toDietRecord(): DietRecord {
    return DietRecord(
        dietRecord.id,
        dietRecord.name,
        dietRecord.dateTime,
        dietRecord.note,
        images.map { it.toRecordImage() }
    )
}

fun RecordImageEntity.toRecordImage(): RecordImage {
    return RecordImage(id, filePath, note)
}

fun StravaSportRecord.toStravaSportEntity(): StravaSportEntity {
    return StravaSportEntity(stravaId, dateTime, name, distance, calories, type, elapsedTimeSec)
}

fun StravaSportEntity.toStravaSport(): StravaSportRecord {
    return StravaSportRecord(id, datetime, name, distance, calories, type, elapsedTimeSec)
}

fun BodyShapeRecord.toBodyShapeRecordEntity(): BodyShapeRecordEntity {
    return BodyShapeRecordEntity(id, name, dateTime, note, weight, bodyFatPercentage)
}

fun BodyShapeRecordWithImages.toBodyShapeRecord(): BodyShapeRecord {
    return BodyShapeRecord(
        bodyShapeRecord.id,
        bodyShapeRecord.name,
        bodyShapeRecord.dateTime,
        bodyShapeRecord.note,
        images.map { it.toRecordImage() },
        bodyShapeRecord.weight,
        bodyShapeRecord.bodyFatPercentage
    )
}