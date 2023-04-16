package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(): Flow<List<BodyShapeRecord>> {
        return recordRepository.getAllBodyShapeRecords()
    }
}