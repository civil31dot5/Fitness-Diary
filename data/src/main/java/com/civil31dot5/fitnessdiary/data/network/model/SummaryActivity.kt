package com.civil31dot5.fitnessdiary.data.network.model
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName



@Serializable
data class SummaryActivity(
    @SerialName("id")
    val id: Long,
    @SerialName("achievement_count")
    val achievementCount: Int,
    @SerialName("athlete")
    val athlete: Athlete,
    @SerialName("athlete_count")
    val athleteCount: Int,
    @SerialName("average_cadence")
    val averageCadence: Double? = null,
    @SerialName("average_heartrate")
    val averageHeartrate: Double? = null,
    @SerialName("average_speed")
    val averageSpeed: Double,
    @SerialName("distance")
    val distance: Double,
    @SerialName("elapsed_time")
    val elapsedTime: Int,
    @SerialName("elev_high")
    val elevHigh: Double? = null,
    @SerialName("elev_low")
    val elevLow: Double? = null,
    @SerialName("end_latlng")
    val endLatlng: List<Double>,
    @SerialName("has_heartrate")
    val hasHeartrate: Boolean,
    @SerialName("kudos_count")
    val kudosCount: Int,
    @SerialName("location_country")
    val locationCountry: String,
    @SerialName("map")
    val map: Map,
    @SerialName("max_heartrate")
    val maxHeartrate: Double? = null,
    @SerialName("max_speed")
    val maxSpeed: Double,
    @SerialName("moving_time")
    val movingTime: Int,
    @SerialName("name")
    val name: String,
    @SerialName("sport_type")
    val sportType: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("start_date_local")
    val startDateLocal: String,
    @SerialName("start_latlng")
    val startLatlng: List<Double>,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("total_elevation_gain")
    val totalElevationGain: Double,
    @SerialName("total_photo_count")
    val totalPhotoCount: Int,
    @SerialName("trainer")
    val trainer: Boolean,
    @SerialName("type")
    val type: String,
    @SerialName("utc_offset")
    val utcOffset: Double,
    @SerialName("visibility")
    val visibility: String,
)

@Serializable
data class Athlete(
    @SerialName("id")
    val id: Int,
    @SerialName("resource_state")
    val resourceState: Int
)

@Serializable
data class Map(
    @SerialName("id")
    val id: String,
    @SerialName("resource_state")
    val resourceState: Int,
    @SerialName("summary_polyline")
    val summaryPolyline: String? = null
)