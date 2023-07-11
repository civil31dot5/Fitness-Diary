package com.civil31dot5.fitnessdiary.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.GetBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DayRecordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDietRecordUseCase: GetDietRecordUseCase,
    private val getStravaSportRecordUseCase: GetStravaSportRecordUseCase,
    private val getBodyShapeRecordUseCase: GetBodyShapeRecordUseCase
) : ViewModel() {

    val selectedDateFlow = savedStateHandle.getStateFlow<String?>(selectedDate, null)
        .filterNotNull()
        .map {
            try {
                LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }catch (e: Exception){
                LocalDate.now()
            }
        }

    val selectedDateRecords = selectedDateFlow
        .filterNotNull()
        .flatMapLatest {
            combine(
                getDietRecordUseCase.invoke(it, it),
                getStravaSportRecordUseCase.invoke(it, it),
                getBodyShapeRecordUseCase.invoke(it, it)
            ) { dietRecords, stravaSports, bodyShapeRecords ->
                (dietRecords + stravaSports + bodyShapeRecords).sortedBy { it.dateTime }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

}