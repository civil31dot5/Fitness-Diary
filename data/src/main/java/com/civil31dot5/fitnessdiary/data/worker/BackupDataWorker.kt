package com.civil31dot5.fitnessdiary.data.worker

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
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

    private val notificationId = 1002

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

        listOf(
            File(appContext.filesDir, "images"),
            File(appContext.dataDir, "databases")
        ).forEach {
            if (!it.isDirectory) return@forEach
            zipFile.addFolder(it, zipParameters)
        }
        zipFile.close()

        appContext.contentResolver.openOutputStream(Uri.parse(outputFilePath))?.use { fileOut ->
            backFile.inputStream().use { fileIn ->
                fileIn.copyTo(fileOut)
            }
        }

        backFile.delete()
    }
}