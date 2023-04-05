package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetMonthStravaSportHistoryUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    suspend operator fun invoke(yearMonth: YearMonth): List<StravaSport>{
        val from = LocalDate.of(yearMonth.year, yearMonth.month, 1)
        val to = LocalDate.of(yearMonth.year, yearMonth.month, yearMonth.lengthOfMonth())
        return stravaRepository.getSportHistory(from, to)
    }
}