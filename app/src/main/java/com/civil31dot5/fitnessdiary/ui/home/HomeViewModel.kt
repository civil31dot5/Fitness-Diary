package com.civil31dot5.fitnessdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.GetMonthRecordStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.RecordStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMonthRecordStatusUseCase: GetMonthRecordStatusUseCase
) : ViewModel() {

    private val selectYearMonth = MutableStateFlow<YearMonth?>(null)

    val recordStatus = selectYearMonth
        .filterNotNull()
        .flatMapLatest { getMonthRecordStatusUseCase.invoke(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun selectYearMonth(yearMonth: YearMonth) {
        selectYearMonth.update { yearMonth }
    }

}