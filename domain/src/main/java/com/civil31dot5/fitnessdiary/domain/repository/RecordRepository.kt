package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDate
import java.time.YearMonth

interface RecordRepository {
    suspend fun addDietRecord(record: DietRecord): Result<Unit>
    fun getAllDietRecords(): Flow<List<DietRecord>>
    fun getDietRecords(from: LocalDate, to: LocalDate): Flow<List<DietRecord>>
    suspend fun deleteDietRecord(record: DietRecord)
    fun getMonthDietRecord(yearMonth: YearMonth): Flow<List<DietRecord>>
    suspend fun addBodyShapeRecord(record: BodyShapeRecord): Result<Unit>
    fun getAllBodyShapeRecords(): Flow<List<BodyShapeRecord>>
    suspend fun deleteBodyShapeRecord(record: BodyShapeRecord)
    fun getBodyShapeRecords(from: LocalDate, to: LocalDate): Flow<List<BodyShapeRecord>>
    suspend fun createWeekDietImage(from: LocalDate, to: LocalDate): File?
}