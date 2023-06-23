package com.civil31dot5.fitnessdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetMonthDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetMonthStravaSportRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonthDietRecordUseCase: GetMonthDietRecordUseCase,
    private val getMonthStravaSportRecordUseCase: GetMonthStravaSportRecordUseCase
) : ViewModel() {


    data class UiState(
        val isLoading: Boolean = false,
        val recordStatus: Map<LocalDate, RecordStatus> = emptyMap()
    )

    data class RecordStatus(
        val hasDietRecord: Boolean = false,
        val hasSportHistory: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val selectYearMonth = MutableStateFlow<YearMonth?>(null)

    fun selectYearMonth(yearMonth: YearMonth) {
        Timber.d("selectYearMonth $yearMonth")
        selectYearMonth.update { yearMonth }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthSportHistory = selectYearMonth.mapNotNull { it }
        .flatMapLatest { getMonthStravaSportRecordUseCase.invoke(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthDietRecord = selectYearMonth.mapNotNull { it }
        .flatMapLatest { getMonthDietRecordUseCase.invoke(it) }


    init {
        viewModelScope.launch {
            monthSportHistory.combine(monthDietRecord) { sportHistory, dietRecords ->

                val recordStatus = _uiState.value.recordStatus.toMutableMap()
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

                _uiState.update { it.copy(isLoading = false, recordStatus = recordStatus) }
            }.collect()
        }
    }

}