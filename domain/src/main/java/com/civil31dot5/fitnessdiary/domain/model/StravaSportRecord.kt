package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

data class StravaSportRecord(
    val stravaId: Long,
    override val dateTime: LocalDateTime,
    override val name: String,
    val distance: Double,
    val calories: Double,
    val type: String,
    val elapsedTimeSec: Long
): Record(stravaId.toString(), name, dateTime, "")
