package com.civil31dot5.fitnessdiary.domain.usecase.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetStravaSportRecordUseCase @Inject constructor(
    private val stravaRepository: StravaRepository
) {

    operator fun invoke(from: LocalDate, to: LocalDate): Flow<ImmutableList<StravaSportRecord>> {
        return stravaRepository.getSportRecord(from, to).map { it.toPersistentList() }
    }
}

