package com.civil31dot5.fitnessdiary.testing.repository

import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class TestBackupRestoreDataRepository: BackupRestoreDataRepository {

    private val backupStatusFlow = MutableSharedFlow<BackupStatus>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    var receivedBackupPath: String = ""
    var receivedRestorePath: String = ""

    var isForceRestoreFail = false

    override fun backup(filePath: String): Flow<BackupStatus> {
        receivedBackupPath = filePath
        return backupStatusFlow
    }

    fun setBackupStatus(status: BackupStatus){
        backupStatusFlow.tryEmit(status)
    }


    override suspend fun restore(filePath: String) {
        receivedRestorePath = filePath
        if (isForceRestoreFail){
            throw RuntimeException("force fail")
        }
    }
}