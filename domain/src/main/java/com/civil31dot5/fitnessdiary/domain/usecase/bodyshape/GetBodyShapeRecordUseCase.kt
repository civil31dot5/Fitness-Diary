package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<BodyShapeRecord>> {
        return recordRepository.getBodyShapeRecords(from, to)
    }
}