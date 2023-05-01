package com.civil31dot5.fitnessdiary.domain.model

data class WeekReport(
    val yearWeek: YearWeek,
    val weekWeight: Double?,
    val weekFatRate: Double?,
    val weekSportCalories: Double
){

    override fun toString(): String {
        return "$yearWeek weight:$weekWeight, fatRate:$weekFatRate, calories: $weekSportCalories"
    }
}