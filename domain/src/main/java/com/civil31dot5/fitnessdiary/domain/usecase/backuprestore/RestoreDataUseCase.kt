package com.civil31dot5.fitnessdiary.domain.usecase.backuprestore

import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import javax.inject.Inject

class RestoreDataUseCase @Inject constructor(
    private val repository: BackupRestoreDataRepository
) {
    suspend operator fun invoke(filePath: String){
        repository.restore(filePath)
    }
}