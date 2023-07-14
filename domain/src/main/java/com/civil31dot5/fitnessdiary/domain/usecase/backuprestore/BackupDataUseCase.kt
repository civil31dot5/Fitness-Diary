package com.civil31dot5.fitnessdiary.domain.usecase.backuprestore

import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupDataUseCase @Inject constructor(
    private val repository: BackupRestoreDataRepository
) {
    operator fun invoke(filePath: String): Flow<BackupStatus> {
        return repository.backup(filePath)
    }
}


sealed interface BackupStatus{
    data class InProgress(val progress: Int): BackupStatus

    object Success: BackupStatus

    object Fail: BackupStatus
}