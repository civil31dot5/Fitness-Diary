package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

data class StravaSport(
    val id: Long,
    val datetime: LocalDateTime,
    val name: String,
    val distance: Double,
    val calories: Double,
    val type: String,
    val elapsedTimeSec: Long
)