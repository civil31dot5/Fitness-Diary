package com.civil31dot5.fitnessdiary.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DayRecordViewModel @Inject constructor(
    private val getDietRecordUseCase: GetDietRecordUseCase,
    private val getStravaSportRecordUseCase: GetStravaSportRecordUseCase
): ViewModel() {

    private val selectedDateFlow = MutableStateFlow<LocalDate?>(null)

    val selectedDateRecords = selectedDateFlow
        .filterNotNull()
        .flatMapLatest {
            getDietRecordUseCase.invoke(it, it)
                .combine(getStravaSportRecordUseCase.invoke(it, it)){ dietRecords, stravaSports ->
                    dietRecords + stravaSports
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())


    fun setDate(date: LocalDate) {
        selectedDateFlow.update { return@update date }
    }



}