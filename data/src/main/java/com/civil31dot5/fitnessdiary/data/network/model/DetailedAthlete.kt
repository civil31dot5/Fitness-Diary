package com.civil31dot5.fitnessdiary.data.network.model
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName
import org.json.JSONObject


@Serializable
data class DetailedAthlete(
    @SerialName("athlete_type")
    val athleteType: Int,
    @SerialName("badge_type_id")
    val badgeTypeId: Int,
    @SerialName("bikes")
    val bikes: List<Bike>,
    @SerialName("city")
    val city: String,
    @SerialName("country")
    val country: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("date_preference")
    val datePreference: String,
    @SerialName("firstname")
    val firstname: String,
    @SerialName("follower_count")
    val followerCount: Int,
    @SerialName("friend_count")
    val friendCount: Int,
    @SerialName("ftp")
    val ftp: Int,
    @SerialName("id")
    val id: Long,
    @SerialName("lastname")
    val lastname: String,
    @SerialName("measurement_preference")
    val measurementPreference: String,
    @SerialName("mutual_friend_count")
    val mutualFriendCount: Int,
    @SerialName("premium")
    val premium: Boolean,
    @SerialName("profile")
    val profile: String,
    @SerialName("profile_medium")
    val profileMedium: String,
    @SerialName("resource_state")
    val resourceState: Int,
    @SerialName("sex")
    val sex: String,
    @SerialName("shoes")
    val shoes: List<Shoe>,
    @SerialName("state")
    val state: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("username")
    val username: String,
    @SerialName("weight")
    val weight: Double
)

@Serializable
data class Bike(
    @SerialName("distance")
    val distance: Int,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("primary")
    val primary: Boolean,
    @SerialName("resource_state")
    val resourceState: Int
)

@Serializable
data class Shoe(
    @SerialName("distance")
    val distance: Int,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("primary")
    val primary: Boolean,
    @SerialName("resource_state")
    val resourceState: Int
)