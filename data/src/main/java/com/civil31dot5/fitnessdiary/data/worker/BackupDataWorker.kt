package com.civil31dot5.fitnessdiary.data.worker

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ExcludeFileFilter
import net.lingala.zip4j.model.ZipParameters
import timber.log.Timber
import java.io.File

@HiltWorker
class BackupDataWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val  workerParams: WorkerParameters,
): CoroutineWorker(appContext, workerParams) {

    companion object{
        private const val notificationId = 1002

        private const val BACKUP_DATA_WORK_NAME = "BackupDataWorker"
        private const val KEY_PROGRESS = "KEY_PROGRESS"
        fun start(context: Context, filePath: String){
            val workRequest = OneTimeWorkRequestBuilder<BackupDataWorker>()
                .setInputData(workDataOf("file_path" to filePath))
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    BACKUP_DATA_WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest
                )
        }

        fun observerProgress(context: Context): Flow<BackupStatus> {
            return callbackFlow {
                val progressLiveData = WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWorkLiveData(BACKUP_DATA_WORK_NAME)

                val observer = Observer<List<WorkInfo>>{
                    val work = it.firstOrNull()
                    work?.let { work ->
                        Timber.d("work state: ${work.state.name}")
                        when(work.state){
                            WorkInfo.State.RUNNING -> {
                                val progress = work.progress.getInt(KEY_PROGRESS, 0)
                                trySend(BackupStatus.InProgress(progress))
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                trySend(BackupStatus.Success)
                                close()
                            }
                            WorkInfo.State.FAILED -> {
                                trySend(BackupStatus.Fail)
                                close()
                            }
                            else -> {}
                        }
                    }

                }
                progressLiveData.observeForever(observer)
                awaitClose { progressLiveData.removeObserver(observer) }
            }
        }

    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            notificationId,
            WorkNotificationUtil.createNotification(applicationContext, "備份資料中...")
        )
    }

    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())
            val outputFilePath = inputData.getString("file_path")
            Timber.d("outputFilePath: $outputFilePath")
            exportData(outputFilePath)
            Timber.d("backup success")
            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            Timber.d("backup fail")
            Result.failure()
        }
    }

    private suspend fun exportData(outputFilePath: String?) = withContext(Dispatchers.IO){

        setProgress(0)

        val backFile = File(appContext.filesDir, "backup.zip")
        if (!backFile.exists()) backFile.createNewFile()

        val zipFile = ZipFile(backFile)
        zipFile.comment = "v1"

        //排除google 相關的db file
        val excludeFileFilter = ExcludeFileFilter{
            it.name.contains("google")
        }

        val zipParameters = ZipParameters().apply {
            this.excludeFileFilter = excludeFileFilter
        }

        setProgress(10)

        listOf(
            File(appContext.filesDir, "images"),
            File(appContext.dataDir, "databases")
        ).forEach {
            if (!it.isDirectory) return@forEach
            zipFile.addFolder(it, zipParameters)
        }
        zipFile.close()

        setProgress(50)

        appContext.contentResolver.openOutputStream(Uri.parse(outputFilePath))?.use { fileOut ->
            backFile.inputStream().use { fileIn ->
                fileIn.copyTo(fileOut)
            }
        }
        setProgress(90)

        backFile.delete()

        setProgress(100)
    }

    private suspend fun setProgress(progress: Int){
        Timber.d("setProgress:$progress")
        setProgress(workDataOf(KEY_PROGRESS to progress))
    }
}