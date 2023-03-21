package com.civil31dot5.fitnessdiary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.GetAllDietRecordUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllDietRecordUseCase: GetAllDietRecordUseCase
): ViewModel() {

    data class UiState(
        val count: Int? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllDietRecordUseCase()
                .stateIn(viewModelScope)
                .collect{ recordList ->
                    _uiState.update { it.copy(count = recordList.size) }
                }
        }
    }

}