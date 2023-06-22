package com.civil31dot5.fitnessdiary.domain.usecase.backuprestore

import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import javax.inject.Inject

class BackupDataUseCase @Inject constructor(
    private val repository: BackupRestoreDataRepository
) {
    operator fun invoke(filePath: String){
        repository.backup(filePath)
    }
}