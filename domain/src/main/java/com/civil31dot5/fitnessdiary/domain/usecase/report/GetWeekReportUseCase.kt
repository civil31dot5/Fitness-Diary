package com.civil31dot5.fitnessdiary.domain.usecase.report

import com.civil31dot5.fitnessdiary.domain.model.WeekReport
import com.civil31dot5.fitnessdiary.domain.model.YearWeek
import com.civil31dot5.fitnessdiary.domain.model.toYearWeek
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.GetBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportHistoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import javax.inject.Inject


class GetWeekReportUseCase @Inject constructor(
    private val getStravaSportHistoryUseCase: GetStravaSportHistoryUseCase,
    private val getBodyShapeRecordUseCase: GetBodyShapeRecordUseCase
) {

    operator fun invoke(numOfWeeks: Int): Flow<List<WeekReport>> {
        val currentWeekFirstDate =
            LocalDate.now().with(WeekFields.ISO.dayOfWeek(), DayOfWeek.MONDAY.value.toLong())

        val searchWeeks = (0 until numOfWeeks)
            .map {
                val targetDate = LocalDate.now().minusWeeks(it.toLong())
                val year = targetDate.year
                val weekNum = targetDate
                    .get(WeekFields.ISO.weekOfWeekBasedYear())
                return@map YearWeek(year, weekNum)
            }

        val searchFromDate = currentWeekFirstDate.minusWeeks(numOfWeeks.toLong())

        return getStravaSportHistoryUseCase.invoke(searchFromDate, LocalDate.now())
            .map { it.groupBy { it.dateTime.toYearWeek() } }
            .combine(
                getBodyShapeRecordUseCase.invoke(searchFromDate, LocalDate.now())
                    .map { it.groupBy { it.dateTime.toYearWeek() } }
            ) { sports, bodyShapeRecords ->

                return@combine searchWeeks.map {
                    val weekSportCalories = sports.getOrDefault(it, emptyList())
                        .sumOf { it.calories }

                    val weekLastWeightRecord = bodyShapeRecords.getOrDefault(it, emptyList())
                        .maxByOrNull { it.dateTime }

                    WeekReport(
                        it,
                        weekLastWeightRecord?.weight,
                        weekLastWeightRecord?.bodyFatPercentage,
                        weekSportCalories
                    )
                }

            }
    }
}

