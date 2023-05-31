package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetMonthStravaSportRecordUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    operator fun invoke(yearMonth: YearMonth): Flow<List<StravaSportRecord>> {
        val from = LocalDate.of(yearMonth.year, yearMonth.month, 1)
        val to = LocalDate.of(yearMonth.year, yearMonth.month, yearMonth.lengthOfMonth())
        return stravaRepository.getSportRecord(from, to)
    }
}