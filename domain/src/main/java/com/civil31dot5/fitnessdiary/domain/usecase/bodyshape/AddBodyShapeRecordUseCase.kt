package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import javax.inject.Inject

class AddBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke(record: BodyShapeRecord): Boolean {
        return recordRepository.addBodyShapeRecord(record).isSuccess
    }
}