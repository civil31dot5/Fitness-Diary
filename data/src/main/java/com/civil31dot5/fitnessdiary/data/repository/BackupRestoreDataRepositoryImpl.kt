package com.civil31dot5.fitnessdiary.data.repository

import android.content.Context
import android.net.Uri
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.civil31dot5.fitnessdiary.data.worker.BackupDataWorker
import com.civil31dot5.fitnessdiary.domain.repository.BackupRestoreDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.lingala.zip4j.ZipFile
import java.io.File
import javax.inject.Inject

class BackupRestoreDataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): BackupRestoreDataRepository {

    override fun backup(filePath: String) {
        val workRequest = OneTimeWorkRequestBuilder<BackupDataWorker>()
            .setInputData(
                workDataOf(
                    "file_path" to filePath,
                )
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "BackupDataWorker",
                ExistingWorkPolicy.KEEP,
                workRequest
            )
    }

    override suspend fun restore(filePath: String) = withContext(Dispatchers.IO){
        val tmpFile = File.createTempFile("restore", null)

        context.contentResolver.openInputStream(Uri.parse(filePath))?.use { fileIs ->
            tmpFile.outputStream().use { fileIs.copyTo(it) }
        }

        val zipFile = ZipFile(tmpFile)
        if (zipFile.comment == "v1"){
            zipFile.extractFile("databases/", context.dataDir.absolutePath)
            zipFile.extractFile("images/", context.filesDir.absolutePath)
        }
    }
}