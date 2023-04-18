package com.civil31dot5.fitnessdiary.data.network.model
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class DetailedActivity(
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
    @SerialName("average_temp")
    val averageTemp: Int? = null,
    @SerialName("calories")
    val calories: Double,
    @SerialName("comment_count")
    val commentCount: Int,
    @SerialName("commute")
    val commute: Boolean,
    @SerialName("description")
    val description: String? = null,
    @SerialName("device_name")
    val deviceName: String,
    @SerialName("display_hide_heartrate_option")
    val displayHideHeartrateOption: Boolean,
    @SerialName("distance")
    val distance: Double,
    @SerialName("elapsed_time")
    val elapsedTime: Long,
    @SerialName("elev_high")
    val elevHigh: Double? = null,
    @SerialName("elev_low")
    val elevLow: Double? = null,
    @SerialName("embed_token")
    val embedToken: String,
    @SerialName("end_latlng")
    val endLatlng: List<Double>,
    @SerialName("external_id")
    val externalId: String,
    @SerialName("flagged")
    val flagged: Boolean,
    @SerialName("from_accepted_tag")
    val fromAcceptedTag: Boolean,
    @SerialName("has_heartrate")
    val hasHeartrate: Boolean,
    @SerialName("has_kudoed")
    val hasKudoed: Boolean,
    @SerialName("heartrate_opt_out")
    val heartrateOptOut: Boolean,
    @SerialName("hide_from_home")
    val hideFromHome: Boolean,
    @SerialName("kudos_count")
    val kudosCount: Int,
    @SerialName("location_country")
    val locationCountry: String,
    @SerialName("manual")
    val manual: Boolean,
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
    @SerialName("start_date")
    val startDate: String,
    @SerialName("start_date_local")
    val startDateLocal: String,
    @SerialName("start_latlng")
    val startLatlng: List<Double>,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("trainer")
    val trainer: Boolean,
    @SerialName("type")
    val type: String,
    @SerialName("utc_offset")
    val utcOffset: Double,

)



