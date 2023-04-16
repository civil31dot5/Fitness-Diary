package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface RecordRepository {
    suspend fun addDietRecord(record: DietRecord): Result<Unit>
    fun getAllDietRecords(): Flow<List<DietRecord>>
    suspend fun deleteDietRecord(record: DietRecord)
    suspend fun getMonthDietRecord(yearMonth: YearMonth): List<DietRecord>
    suspend fun addBodyShapeRecord(record: BodyShapeRecord): Result<Unit>
    fun getAllBodyShapeRecords(): Flow<List<BodyShapeRecord>>
    suspend fun deleteBodyShapeRecord(record: BodyShapeRecord)
}