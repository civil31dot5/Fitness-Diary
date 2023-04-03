package com.civil31dot5.fitnessdiary.data.network

import com.civil31dot5.fitnessdiary.data.network.model.DetailedActivity
import com.civil31dot5.fitnessdiary.data.network.model.DetailedAthlete
import com.civil31dot5.fitnessdiary.data.network.model.SummaryActivity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalAccessor
import javax.inject.Inject


interface StravaApi {

    suspend fun getDetailedAthlete(): DetailedAthlete

    suspend fun getSummaryActivities(from: LocalDate, to: LocalDate): List<SummaryActivity>

    suspend fun getDetailActivity(id: Long): DetailedActivity

}


class StravaApiImpl @Inject constructor(
    private val httpClient: HttpClient,
): StravaApi {

    override suspend fun getDetailedAthlete(): DetailedAthlete = withContext(Dispatchers.IO){
        return@withContext httpClient.get("athlete").body()
    }


    override suspend fun getSummaryActivities(
        from: LocalDate,
        to: LocalDate
    ): List<SummaryActivity> = withContext(Dispatchers.IO){

        val fromSec = from.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val toSec = to.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()


        return@withContext httpClient.get {
            url("athlete/activities")
            parameter("after", fromSec)
            parameter("before", toSec)
        }.body()
    }

    override suspend fun getDetailActivity(id: Long): DetailedActivity = withContext(Dispatchers.IO){
        return@withContext httpClient.get("activities/$id").body()
    }
}