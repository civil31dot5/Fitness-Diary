package com.civil31dot5.fitnessdiary.domain.usecase.diet

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDietRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {
    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<DietRecord>> {
        return repository.getDietRecords(from, to)
    }
}