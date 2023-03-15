package com.civil31dot5.fitnessdiary.data.repository

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(): RecordRepository {

    override suspend fun addDietRecord(record: DietRecord): Boolean {
        delay(2000)
        return true
    }
}