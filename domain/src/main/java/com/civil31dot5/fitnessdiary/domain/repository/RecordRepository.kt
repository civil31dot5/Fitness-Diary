package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.DietRecord

interface RecordRepository {
    suspend fun addDietRecord(record: DietRecord): Result<Unit>
    suspend fun getAllDietRecords(): List<DietRecord>
}