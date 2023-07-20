package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBodyShapeRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(): Flow<ImmutableList<BodyShapeRecord>> {
        return recordRepository.getAllBodyShapeRecords().map { it.toPersistentList() }
    }
}