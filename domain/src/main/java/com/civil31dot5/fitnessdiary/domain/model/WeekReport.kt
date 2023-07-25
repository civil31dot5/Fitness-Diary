package com.civil31dot5.fitnessdiary.domain.model

data class WeekReport(
    val yearWeek: YearWeek,
    val weekWeight: Double?,
    val weekFatRate: Double?,
    val weekSportCalories: Double
) {

    val fatMass: Double?
        get() {
            if (weekWeight == null) return null
            if (weekFatRate == null) return null
            return weekWeight * (weekFatRate/100.0)
        }

    override fun toString(): String {
        return "$yearWeek weight:$weekWeight, fatRate:$weekFatRate, calories: $weekSportCalories"
    }
}