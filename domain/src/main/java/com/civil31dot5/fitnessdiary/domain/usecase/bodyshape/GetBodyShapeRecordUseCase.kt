package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(from: LocalDate, to: LocalDate): Flow<ImmutableList<BodyShapeRecord>> {
        return recordRepository.getBodyShapeRecords(from, to).map { it.toPersistentList() }
    }
}