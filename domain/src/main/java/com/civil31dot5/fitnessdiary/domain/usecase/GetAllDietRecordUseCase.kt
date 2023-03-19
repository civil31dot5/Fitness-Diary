package com.civil31dot5.fitnessdiary.domain.usecase

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import javax.inject.Inject

class GetAllDietRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    suspend operator fun invoke(): List<DietRecord>{
        return repository.getAllDietRecords()
    }
}