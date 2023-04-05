package com.civil31dot5.fitnessdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetAllDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetMonthDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetMonthStravaSportHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonthDietRecordUseCase: GetMonthDietRecordUseCase,
    private val getMonthStravaSportHistoryUseCase: GetMonthStravaSportHistoryUseCase
): ViewModel() {

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

    fun selectYearMonth(yearMonth: YearMonth) {
        refreshMonthRecordStatus(yearMonth)
    }

    private fun refreshMonthRecordStatus(yearMonth: YearMonth) = viewModelScope.launch{

        _uiState.update { it.copy(isLoading = true) }

        val dietRecordsDeferred = async {getMonthDietRecordUseCase.invoke(yearMonth) }
        val sportHistoryDeferred = async { getMonthStravaSportHistoryUseCase.invoke(yearMonth) }

        val dietRecords = dietRecordsDeferred.await()
        val sportHistory = sportHistoryDeferred.await()

        val recordStatus = _uiState.value.recordStatus.toMutableMap()

        dietRecords.forEach {

            if (recordStatus.containsKey(it.dateTime.toLocalDate())){
                recordStatus[it.dateTime.toLocalDate()] =  recordStatus[it.dateTime.toLocalDate()]!!.copy(hasDietRecord = true)
            }else{
                recordStatus[it.dateTime.toLocalDate()] =  RecordStatus(hasDietRecord = true)
            }

        }

        sportHistory.forEach {

            if (recordStatus.containsKey(it.datetime.toLocalDate())){
                recordStatus[it.datetime.toLocalDate()] =  recordStatus[it.datetime.toLocalDate()]!!.copy(hasSportHistory = true)
            }else{
                recordStatus[it.datetime.toLocalDate()] =  RecordStatus(hasSportHistory = true)
            }
        }

        _uiState.update { it.copy(isLoading = false, recordStatus = recordStatus) }
    }




}