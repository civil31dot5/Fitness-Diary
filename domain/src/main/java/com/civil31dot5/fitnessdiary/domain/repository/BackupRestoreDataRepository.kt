package com.civil31dot5.fitnessdiary.domain.repository

interface BackupRestoreDataRepository {
    fun backup(filePath: String)

    suspend fun restore(filePath: String)
}