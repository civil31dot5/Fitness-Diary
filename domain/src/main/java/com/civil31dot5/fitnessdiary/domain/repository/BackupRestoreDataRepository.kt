package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import kotlinx.coroutines.flow.Flow

interface BackupRestoreDataRepository {
    fun backup(filePath: String): Flow<BackupStatus>

    suspend fun restore(filePath: String)
}