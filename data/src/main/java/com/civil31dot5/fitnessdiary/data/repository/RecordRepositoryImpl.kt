package com.civil31dot5.fitnessdiary.data.repository

import com.civil31dot5.fitnessdiary.data.*
import com.civil31dot5.fitnessdiary.data.database.RecordDao
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao,
    private val imageFileProcessor: RecordImageFileProcessor
) : RecordRepository {

    override suspend fun addDietRecord(record: DietRecord): Result<Unit> = kotlin.runCatching {

        recordDao.insertDietRecord(record.toDietRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.copyFile(image.filePath, "diet")
                .fold(
                    onSuccess = { newFilePath ->
                        recordDao.insertRecordImage(
                            image.toRecordImageEntity(
                                record.id,
                                newFilePath
                            )
                        )
                    },
                    onFailure = { Timber.e(it) }
                )
        }
    }

    override fun getAllDietRecords(): Flow<List<DietRecord>> {
        return recordDao.getAllDietRecord().map {
            it.map { it.toDietRecord() }
        }
    }

    override fun getDietRecords(from: LocalDate, to: LocalDate): Flow<List<DietRecord>> {
        val fromDateTime = LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0)
        val toDateTime = LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59)
        return recordDao.searchDietRecord(fromDateTime, toDateTime)
            .map { it.map { it.toDietRecord() } }
    }

    override suspend fun deleteDietRecord(record: DietRecord) {

        recordDao.deleteDietRecord(record.toDietRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.deleteFile(image.filePath)
            recordDao.deleteRecordImage(image.toRecordImageEntity(record.id, ""))
        }
    }

    override fun getMonthDietRecord(yearMonth: YearMonth): Flow<List<DietRecord>> {
        val from = LocalDateTime.of(yearMonth.year, yearMonth.month, 1, 0, 0)
        val to =
            LocalDateTime.of(yearMonth.year, yearMonth.month, yearMonth.lengthOfMonth(), 23, 59)
        return recordDao.searchDietRecord(from, to).map { it.map { it.toDietRecord() } }
    }

    override suspend fun addBodyShapeRecord(record: BodyShapeRecord): Result<Unit> =
        kotlin.runCatching {

            recordDao.insertBodyShapeRecord(record.toBodyShapeRecordEntity())

            record.images.forEach { image ->
                imageFileProcessor.copyFile(image.filePath, "body_shape")
                    .fold(
                        onSuccess = { newFilePath ->
                            recordDao.insertRecordImage(
                                image.toRecordImageEntity(
                                    record.id,
                                    newFilePath
                                )
                            )
                        },
                        onFailure = { Timber.e(it) }
                    )
            }

        }

    override fun getAllBodyShapeRecords(): Flow<List<BodyShapeRecord>> {
        return recordDao.getAllBodyShapeRecord().map { it.map { it.toBodyShapeRecord() } }
    }

    override suspend fun deleteBodyShapeRecord(record: BodyShapeRecord) {
        recordDao.deleteBodyShapeRecord(record.toBodyShapeRecordEntity())

        record.images.forEach { image ->
            imageFileProcessor.deleteFile(image.filePath)
            recordDao.deleteRecordImage(image.toRecordImageEntity(record.id, ""))
        }
    }

    override fun getBodyShapeRecords(from: LocalDate, to: LocalDate): Flow<List<BodyShapeRecord>> {
        val fromDateTime = LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0)
        val toDateTime = LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59)
        return recordDao.getBodyShapeRecord(fromDateTime, toDateTime)
            .map { it.map { it.toBodyShapeRecord() } }
    }
}