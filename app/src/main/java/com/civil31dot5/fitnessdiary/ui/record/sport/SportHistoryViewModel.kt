package com.civil31dot5.fitnessdiary.ui.record.sport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import com.civil31dot5.fitnessdiary.domain.usecase.sport.ConnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.DisconnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaConnectStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SportHistoryViewModel @Inject constructor(
    private val getStravaConnectStatusUseCase: GetStravaConnectStatusUseCase,
    private val connectStravaUseCase: ConnectStravaUseCase,
    private val disconnectStravaUseCase: DisconnectStravaUseCase,
    private val getStravaSportHistoryUseCase: GetStravaSportHistoryUseCase
): ViewModel() {

    data class UiState(
        val hasConnectStrava: Boolean? = null,
        val sportHistory: List<StravaSport> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshConnectStatus()

        viewModelScope.launch {
            uiState.map { it.hasConnectStrava == true }
                .filter { it }
                .take(1)
                .collect{
                    val to = LocalDate.now()
                    val from = to.minusWeeks(1)
                    val result = getStravaSportHistoryUseCase(from, to)
                    _uiState.update { it.copy(sportHistory = result) }
                }
        }
    }

    fun connectStrava() = viewModelScope.launch{
        connectStravaUseCase()
        refreshConnectStatus()
    }

    fun disconnectStrava() {
        disconnectStravaUseCase()
        refreshConnectStatus()
    }

    private fun refreshConnectStatus() = viewModelScope.launch{
        _uiState.update { it.copy(hasConnectStrava = getStravaConnectStatusUseCase() == StravaConnectStatus.Connected) }
    }
}