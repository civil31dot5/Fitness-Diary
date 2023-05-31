package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetStravaSportRecordUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<StravaSportRecord>> {
        return stravaRepository.getSportRecord(from, to)
    }
}

