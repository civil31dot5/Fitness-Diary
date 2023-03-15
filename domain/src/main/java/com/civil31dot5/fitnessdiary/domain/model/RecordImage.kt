package com.civil31dot5.fitnessdiary.domain.model

import java.io.File

data class RecordImage(
    val id: Long,
    val file: File,
    val note: String = ""
)