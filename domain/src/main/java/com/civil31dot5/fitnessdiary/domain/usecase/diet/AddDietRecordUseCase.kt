package com.civil31dot5.fitnessdiary.domain.usecase.diet

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import javax.inject.Inject

class AddDietRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {
    suspend operator fun invoke(record: DietRecord): Boolean {
        return repository.addDietRecord(record).isSuccess
    }
}