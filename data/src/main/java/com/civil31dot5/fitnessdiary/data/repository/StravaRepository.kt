package com.civil31dot5.fitnessdiary.data.repository

import android.content.Context
import androidx.work.*
import com.civil31dot5.fitnessdiary.data.database.StravaSportDao
import com.civil31dot5.fitnessdiary.data.network.StravaApi
import com.civil31dot5.fitnessdiary.data.toStravaSport
import com.civil31dot5.fitnessdiary.data.toStravaSportEntity
import com.civil31dot5.fitnessdiary.data.worker.SyncStravaSportHistoryWorker
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StravaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stravaApi: StravaApi,
    private val stravaSportDao: StravaSportDao
) : StravaRepository {

    override suspend fun syncSportRecord(from: LocalDate, to: LocalDate) {

        val summaryActivities = stravaApi.getSummaryActivities(from, to)

        summaryActivities.map { summary ->
            val local = stravaSportDao.queryById(summary.id)
            if (local != null) {
                return@map local.toStravaSport()
            }

            val detailedActivity = stravaApi.getDetailActivity(summary.id)
            val stravaSport = StravaSportRecord(
                summary.id,
                LocalDateTime.parse(
                    summary.startDateLocal.replace("Z", ""),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                ),
                summary.name,
                detailedActivity.distance,
                detailedActivity.calories,
                detailedActivity.type,
                detailedActivity.elapsedTime
            )

            stravaSportDao.insertStravaSport(stravaSport.toStravaSportEntity())
        }
    }

    override fun getSportRecord(from: LocalDate, to: LocalDate): Flow<List<StravaSportRecord>> {

        val workRequest = OneTimeWorkRequestBuilder<SyncStravaSportHistoryWorker>()
            .setInputData(
                workDataOf(
                    "from" to from.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    "to" to to.format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "SyncStravaSportHistoryWorker",
                ExistingWorkPolicy.KEEP,
                workRequest
            )

        return stravaSportDao.query(
            LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0),
            LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59),
        ).map { it.map { it.toStravaSport() } }
    }

}