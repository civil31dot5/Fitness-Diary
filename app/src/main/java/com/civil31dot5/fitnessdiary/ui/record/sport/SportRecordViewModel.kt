package com.civil31dot5.fitnessdiary.ui.record.sport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.usecase.sport.ConnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.DisconnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaConnectStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SportRecordViewModel @Inject constructor(
    private val getStravaConnectStatusUseCase: GetStravaConnectStatusUseCase,
    private val connectStravaUseCase: ConnectStravaUseCase,
    private val disconnectStravaUseCase: DisconnectStravaUseCase,
    private val getStravaSportRecordUseCase: GetStravaSportRecordUseCase
) : ViewModel() {

    data class UiState(
        val hasConnectStrava: Boolean? = null,
        val sportHistory: List<StravaSportRecord> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshConnectStatus()

        viewModelScope.launch {
            uiState.map { it.hasConnectStrava == true }
                .filter { it }
                .take(1)
                .map {
                    val to = LocalDate.now()
                    val from = to.minusMonths(2)
                    getStravaSportRecordUseCase(from, to)
                }
                .first()
                .collect { result ->
                    _uiState.update { it.copy(sportHistory = result.sortedByDescending { it.dateTime }) }
                }
        }
    }

    fun connectStrava() = viewModelScope.launch {
        connectStravaUseCase()
        refreshConnectStatus()
    }

    fun disconnectStrava() {
        disconnectStravaUseCase()
        refreshConnectStatus()
    }

    private fun refreshConnectStatus() = viewModelScope.launch {
        _uiState.update { it.copy(hasConnectStrava = getStravaConnectStatusUseCase() == StravaConnectStatus.Connected) }
    }
}