package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

data class DietRecord(
    val id: Long,
    val datetime: LocalDateTime,
    val images: List<RecordImage> = emptyList(),
    val note: String = ""
)