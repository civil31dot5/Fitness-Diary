package com.civil31dot5.fitnessdiary.data

import com.civil31dot5.fitnessdiary.data.database.DietRecordEntity
import com.civil31dot5.fitnessdiary.data.database.DietRecordWithImages
import com.civil31dot5.fitnessdiary.data.database.RecordImageEntity
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage


fun DietRecord.toDietRecordEntity(): DietRecordEntity {
    return DietRecordEntity(id, name, dateTime, note)
}

fun RecordImage.toRecordImageEntity(recordId: String, newFilePath: String): RecordImageEntity{
    return RecordImageEntity(id, recordId, newFilePath, note)
}

fun DietRecordWithImages.toDietRecord(): DietRecord{
    return DietRecord(
        dietRecord.id,
        dietRecord.name,
        dietRecord.dateTime,
        dietRecord.note,
        images.map { it.toRecordImage() }
    )
}

fun RecordImageEntity.toRecordImage(): RecordImage{
    return RecordImage(id, filePath, note)
}