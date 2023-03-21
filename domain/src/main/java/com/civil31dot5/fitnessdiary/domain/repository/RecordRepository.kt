package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun addDietRecord(record: DietRecord): Result<Unit>
    fun getAllDietRecords(): Flow<List<DietRecord>>
    suspend fun deleteDietRecord(record: DietRecord)
}