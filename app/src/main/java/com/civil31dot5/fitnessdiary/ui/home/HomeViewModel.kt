package com.civil31dot5.fitnessdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.YearWeek
import com.civil31dot5.fitnessdiary.domain.usecase.GetMonthRecordStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.diet.CreateWeekDietImageUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.report.GetWeekReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonthRecordStatusUseCase: GetMonthRecordStatusUseCase,
    private val getWeekReportUseCase: GetWeekReportUseCase
) : ViewModel() {

    private val selectYearMonth = MutableStateFlow<YearMonth?>(null)

    val recordStatus = selectYearMonth
        .filterNotNull()
        .flatMapLatest { getMonthRecordStatusUseCase.invoke(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    val fatMassHistory = getWeekReportUseCase.invoke(12)
        .map {
            it.filter { it.fatMass != null }
                .map { FatMassHistory(it.yearWeek, it.fatMass!!) }
                .sortedBy { it.yearWeek }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())


    fun selectYearMonth(yearMonth: YearMonth) {
        selectYearMonth.update { yearMonth }
    }

    data class FatMassHistory(val yearWeek: YearWeek, val fatMass: Double)
}