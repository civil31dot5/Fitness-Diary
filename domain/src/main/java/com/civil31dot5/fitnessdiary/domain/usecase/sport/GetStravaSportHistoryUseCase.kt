package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import java.time.LocalDate
import javax.inject.Inject

class GetStravaSportHistoryUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    suspend operator fun invoke(from: LocalDate, to: LocalDate): List<StravaSport>{
        return stravaRepository.getSportHistory(from, to)
    }
}

