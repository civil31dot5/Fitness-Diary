package com.civil31dot5.fitnessdiary.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.report.GetWeekReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getWeekReportUseCase: GetWeekReportUseCase,
) : ViewModel() {

    val weekReport = getWeekReportUseCase.invoke(20)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())



}