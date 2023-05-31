package com.civil31dot5.fitnessdiary.domain.usecase.diet

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import javax.inject.Inject


class DeleteDietRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke(record: DietRecord) {
        recordRepository.deleteDietRecord(record)
    }
}