package com.civil31dot5.fitnessdiary.testing.repository

import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestBackupRestoreDataRepository: BackupRestoreDataRepository {
    override fun backup(filePath: String): Flow<BackupStatus> {
        return flow {
            emit(BackupStatus.InProgress(0))
            emit(BackupStatus.InProgress(10))
            emit(BackupStatus.InProgress(50))
            emit(BackupStatus.InProgress(90))
            emit(BackupStatus.InProgress(100))
            emit(BackupStatus.Success)
        }
    }

    override suspend fun restore(filePath: String) {

    }
}