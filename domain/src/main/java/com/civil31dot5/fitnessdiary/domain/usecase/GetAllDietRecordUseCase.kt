package com.civil31dot5.fitnessdiary.domain.usecase

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDietRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    operator fun invoke(): Flow<List<DietRecord>>{
        return repository.getAllDietRecords()
    }
}