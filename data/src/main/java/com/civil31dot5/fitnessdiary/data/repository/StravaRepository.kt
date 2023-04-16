package com.civil31dot5.fitnessdiary.data.repository

import com.civil31dot5.fitnessdiary.data.database.StravaSportDao
import com.civil31dot5.fitnessdiary.data.network.StravaApi
import com.civil31dot5.fitnessdiary.data.toStravaSport
import com.civil31dot5.fitnessdiary.data.toStravaSportEntity
import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class StravaRepositoryImpl @Inject constructor(
    private val stravaApi: StravaApi,
    private val stravaSportDao: StravaSportDao
): StravaRepository {

    override suspend fun getSportHistory(from: LocalDate, to: LocalDate): List<StravaSport> {

//        val summaryActivities = stravaApi.getSummaryActivities(from, to)
//
//        return summaryActivities.map { summary ->
//            val local = stravaSportDao.queryById(summary.id)
//            if (local != null){
//                return@map local.toStravaSport()
//            }
//
//            val detailedActivity = stravaApi.getDetailActivity(summary.id)
//            val stravaSport = StravaSport(
//                summary.id,
//                LocalDateTime.parse(
//                    summary.startDateLocal.replace("Z", ""),
//                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
//                ),
//                summary.name,
//                detailedActivity.distance,
//                detailedActivity.calories,
//                detailedActivity.type,
//                detailedActivity.elapsedTime
//            )
//
//            stravaSportDao.insertStravaSport(stravaSport.toStravaSportEntity())
//
//            return@map stravaSport
//        }



        return stravaSportDao.query(
            LocalDateTime.of(from.year, from.month, from.dayOfMonth, 0, 0),
            LocalDateTime.of(to.year, to.month, to.dayOfMonth, 23, 59),
        ).map { it.toStravaSport() }
    }

}