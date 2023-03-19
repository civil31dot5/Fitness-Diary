package com.civil31dot5.fitnessdiary.data.repository

import android.content.Context
import com.civil31dot5.fitnessdiary.data.RecordImageFileProcessor
import com.civil31dot5.fitnessdiary.data.database.RecordDao
import com.civil31dot5.fitnessdiary.data.toDietRecord
import com.civil31dot5.fitnessdiary.data.toDietRecordEntity
import com.civil31dot5.fitnessdiary.data.toRecordImageEntity
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao,
    private val imageFileProcessor: RecordImageFileProcessor
): RecordRepository {

    override suspend fun addDietRecord(record: DietRecord): Result<Unit> = kotlin.runCatching{

        recordDao.insertDietRecord(record.toDietRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.copyFile(image.filePath, "diet")
                .fold(
                    onSuccess = { newFilePath ->
                        recordDao.insertRecordImage(image.toRecordImageEntity(record.id, newFilePath))
                    },
                    onFailure = { Timber.e(it) }
                )
        }
    }

    override suspend fun getAllDietRecords(): List<DietRecord> {
        return recordDao.getAllDietRecord().map { it.toDietRecord() }
    }
}