package com.civil31dot5.fitnessdiary.domain.usecase.bodyshape

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetMonthBodyShapeRecordUseCase @Inject constructor(
    private val getBodyShapeRecordUseCase: GetBodyShapeRecordUseCase
){
    operator fun invoke(yearMonth: YearMonth): Flow<ImmutableList<BodyShapeRecord>>{
        return getBodyShapeRecordUseCase.invoke(
            LocalDate.of(yearMonth.year, yearMonth.monthValue, 1),
            LocalDate.of(yearMonth.year, yearMonth.monthValue, yearMonth.lengthOfMonth())
        )
    }
}