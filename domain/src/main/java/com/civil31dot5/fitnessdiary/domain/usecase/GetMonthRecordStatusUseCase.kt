package com.civil31dot5.fitnessdiary.domain.usecase

import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetMonthDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetMonthStravaSportRecordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetMonthRecordStatusUseCase @Inject constructor(
    private val getMonthDietRecordUseCase: GetMonthDietRecordUseCase,
    private val getMonthStravaSportRecordUseCase: GetMonthStravaSportRecordUseCase
) {

    operator fun invoke(month: YearMonth): Flow<Map<LocalDate, RecordStatus>> {

        return combine(
            getMonthDietRecordUseCase.invoke(month),
            getMonthStravaSportRecordUseCase.invoke(month)
        ){ dietRecords, sportHistory ->

            val recordStatus = mutableMapOf<LocalDate, RecordStatus>()

            dietRecords.forEach {

                if (recordStatus.containsKey(it.dateTime.toLocalDate())) {
                    recordStatus[it.dateTime.toLocalDate()] =
                        recordStatus[it.dateTime.toLocalDate()]!!.copy(hasDietRecord = true)
                } else {
                    recordStatus[it.dateTime.toLocalDate()] = RecordStatus(hasDietRecord = true)
                }

            }

            sportHistory.forEach {

                if (recordStatus.containsKey(it.dateTime.toLocalDate())) {
                    recordStatus[it.dateTime.toLocalDate()] =
                        recordStatus[it.dateTime.toLocalDate()]!!.copy(hasSportHistory = true)
                } else {
                    recordStatus[it.dateTime.toLocalDate()] =
                        RecordStatus(hasSportHistory = true)
                }
            }

            return@combine recordStatus
        }

    }

}

data class RecordStatus(
    val hasDietRecord: Boolean = false,
    val hasSportHistory: Boolean = false
)