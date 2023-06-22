package com.civil31dot5.fitnessdiary.ui.backuprestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupDataUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.RestoreDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase
): ViewModel() {
    fun backupData(filePath: String) {
        Timber.d("backup file path $filePath")
        backupDataUseCase.invoke(filePath)
    }

    fun restoreData(filePath: String) = viewModelScope.launch {
        Timber.d("restore data begin")
        restoreDataUseCase.invoke(filePath)
        Timber.d("restore data end")
    }

}