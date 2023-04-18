package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetStravaSportHistoryUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<StravaSport>> {
        return stravaRepository.getSportHistory(from, to)
    }
}

