package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

data class ShapeRecord(
    val id: Long,
    val datetime: LocalDateTime,
    val images: List<RecordImage> = emptyList(),
    val weight: Double,
    val bodyFatPercentage: Double? = null,
    val note: String = "",
    val customField: Map<String, String>? = null
)
