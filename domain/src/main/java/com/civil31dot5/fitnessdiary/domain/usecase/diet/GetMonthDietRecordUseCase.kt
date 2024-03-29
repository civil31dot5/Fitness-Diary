package com.civil31dot5.fitnessdiary.domain.usecase.diet

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetMonthDietRecordUseCase @Inject constructor(
    private val repository: RecordRepository
) {

    operator fun invoke(yearMonth: YearMonth): Flow<List<DietRecord>> {
        return repository.getMonthDietRecord(yearMonth)
    }

}