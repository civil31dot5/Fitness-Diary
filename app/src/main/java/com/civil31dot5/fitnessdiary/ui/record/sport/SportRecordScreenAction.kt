package com.civil31dot5.fitnessdiary.ui.record.sport

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.usecase.sport.ConnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.DisconnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaConnectStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SportRecordActionViewModel @Inject constructor(
    private val getStravaConnectStatusUseCase: GetStravaConnectStatusUseCase,
    private val connectStravaUseCase: ConnectStravaUseCase,
    private val disconnectStravaUseCase: DisconnectStravaUseCase
) : ViewModel() {

    val isStravaConnected = MutableStateFlow<Boolean>(false)

    init { refresh() }

    fun connectStrava() = viewModelScope.launch {
        connectStravaUseCase()
        refresh()
    }

    fun disconnectStrava() {
        disconnectStravaUseCase()
        refresh()
    }

    private fun refresh(){
        isStravaConnected.update { getStravaConnectStatusUseCase() == StravaConnectStatus.Connected }
    }

}

@Composable
fun SportRecordScreenAction(
    viewModel: SportRecordActionViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isStravaConnected.collectAsStateWithLifecycle()

    if (isConnected) {
        TextButton(onClick = { viewModel.disconnectStrava() }) {
            Text(text = stringResource(id = R.string.disconnect_strava))
        }
    } else {
        TextButton(onClick = { viewModel.connectStrava() }) {
            Text(text = stringResource(id = R.string.connect_strava))
        }
    }
}