package com.civil31dot5.fitnessdiary.ui.record.weekdiet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.diet.CreateWeekDietImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeekDietViewModel @Inject constructor(
    private val createWeekDietImageUseCase: CreateWeekDietImageUseCase
): ViewModel() {

    val weekDietImageFile = flow<File?> {
        createWeekDietImageUseCase.invoke(
            from = LocalDate.now().minusDays(6),
            to = LocalDate.now()
        ).let {
            emit(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)



}