package com.civil31dot5.fitnessdiary.ui.record.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.usecase.DeleteDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.GetAllDietRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietRecordHistoryViewModel @Inject constructor(
    private val getAllDietRecordUseCase: GetAllDietRecordUseCase,
    private val deleteDietRecordUseCase: DeleteDietRecordUseCase
): ViewModel() {

    val dietRecordList = getAllDietRecordUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteDietRecord(dietRecord: DietRecord) = viewModelScope.launch {
        deleteDietRecordUseCase(dietRecord)
    }
}