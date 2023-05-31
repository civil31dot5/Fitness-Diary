package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import javax.inject.Inject

class DeleteBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke(record: BodyShapeRecord) {
        return recordRepository.deleteBodyShapeRecord(record)
    }
}