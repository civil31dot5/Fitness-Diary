package com.civil31dot5.fitnessdiary.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@HiltWorker
class SyncStravaSportHistoryWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val stravaRepository: StravaRepository
) : CoroutineWorker(appContext, workerParams) {

    private val notificationId = 1001

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            notificationId,
            WorkNotificationUtil.createNotification(applicationContext, "同步Strava中...")
        )
    }

    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())

            val from =
                LocalDate.parse(inputData.getString("from"), DateTimeFormatter.ISO_LOCAL_DATE)
            val to = LocalDate.parse(inputData.getString("to"), DateTimeFormatter.ISO_LOCAL_DATE)
            stravaRepository.syncSportRecord(from, to)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

}