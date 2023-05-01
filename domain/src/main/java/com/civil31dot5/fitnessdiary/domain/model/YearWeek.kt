package com.civil31dot5.fitnessdiary.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

data class YearWeek(
    val year: Int,
    val week: Int
) : Comparable<YearWeek> {

    private val num: Int
    get() {
        return "$year$week".toInt()
    }

    override fun compareTo(other: YearWeek): Int {
        return num - other.num
    }

    override fun toString(): String {
        return "$year-$week"
    }

    fun getFirstDate(): LocalDate{
        return LocalDate.now()
            .withYear(year)
            .with(WeekFields.ISO.weekOfWeekBasedYear(), week.toLong())
            .with(WeekFields.ISO.dayOfWeek(), DayOfWeek.MONDAY.value.toLong())
    }
}

fun LocalDateTime.toYearWeek(): YearWeek{

    val weekNum = toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear())

    return YearWeek(year, weekNum)
}