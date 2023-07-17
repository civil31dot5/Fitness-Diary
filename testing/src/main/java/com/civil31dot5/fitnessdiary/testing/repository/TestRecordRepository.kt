package com.civil31dot5.fitnessdiary.testing.repository

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

class TestRecordRepository: RecordRepository {

    private val dietRecordTestData = MutableStateFlow<List<DietRecord>>(emptyList())
    private val bodyShapeRecordTestData = MutableStateFlow<List<BodyShapeRecord>>(emptyList())

    fun setDietRecordTestData(data: List<DietRecord>){
        dietRecordTestData.update { data }
    }

    fun setBodyShapeRecordTestData(data: List<BodyShapeRecord>){
        bodyShapeRecordTestData.update { data }
    }

    override suspend fun addDietRecord(record: DietRecord): Result<Unit> {
        dietRecordTestData.update { it + record }
        return Result.success(Unit)
    }

    override fun getAllDietRecords(): Flow<List<DietRecord>> {
        return dietRecordTestData
    }

    override fun getDietRecords(from: LocalDate, to: LocalDate): Flow<List<DietRecord>> {
        return dietRecordTestData
    }

    var recentDeleteDietRecord:DietRecord? = null
    override suspend fun deleteDietRecord(record: DietRecord) {
        recentDeleteDietRecord = record
        dietRecordTestData.update { it.toMutableList().also { it.remove(record) } }
    }

    override fun getMonthDietRecord(yearMonth: YearMonth): Flow<List<DietRecord>> {
        return dietRecordTestData
    }


    var recentAddBodyShapeRecord:BodyShapeRecord? = null
    override suspend fun addBodyShapeRecord(record: BodyShapeRecord): Result<Unit> {
        recentAddBodyShapeRecord = record
        bodyShapeRecordTestData.update { it + record }
        return Result.success(Unit)
    }

    override fun getAllBodyShapeRecords(): Flow<List<BodyShapeRecord>> {
        return bodyShapeRecordTestData
    }

    var recentDeleteBodyShapeRecord:BodyShapeRecord? = null
    override suspend fun deleteBodyShapeRecord(record: BodyShapeRecord) {
        recentDeleteBodyShapeRecord = record
        bodyShapeRecordTestData.update { it.toMutableList().also { it.remove(record) } }

    }

    override fun getBodyShapeRecords(from: LocalDate, to: LocalDate): Flow<List<BodyShapeRecord>> {
        return bodyShapeRecordTestData
    }
}