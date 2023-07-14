package com.civil31dot5.fitnessdiary.ui.backuprestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupDataUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.RestoreDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase
) : ViewModel() {
    sealed interface UiState {
        object NoTaskRunning: UiState

        object TaskRunning: UiState

        object BackupDataTaskSuccess: UiState

        object RestoreDataTaskSuccess: UiState

        object TaskFail: UiState

    }

    private val _uiState = MutableStateFlow<UiState>(UiState.NoTaskRunning)
    val uiState = _uiState.asStateFlow()

    fun backupData(filePath: String) = viewModelScope.launch {
        Timber.d("backup file path $filePath")
        backupDataUseCase.invoke(filePath)
            .collect { backupStatus ->

                val newState = when(backupStatus){
                    is BackupStatus.InProgress -> UiState.TaskRunning
                    BackupStatus.Fail -> UiState.TaskFail
                    BackupStatus.Success -> UiState.BackupDataTaskSuccess
                }

                _uiState.update { newState }
            }

    }

    fun restoreData(filePath: String) = viewModelScope.launch {
        try {
            _uiState.update { UiState.TaskRunning }
            restoreDataUseCase.invoke(filePath)
            _uiState.update { UiState.RestoreDataTaskSuccess }
        }catch (e:Exception){
            _uiState.update { UiState.TaskFail }
        }

    }

    fun messageShown() {
        _uiState.update { UiState.NoTaskRunning }
    }

}