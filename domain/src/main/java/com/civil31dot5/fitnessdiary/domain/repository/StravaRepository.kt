package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import java.time.LocalDate

interface StravaRepository {
    suspend fun getSportHistory(from: LocalDate, to: LocalDate): List<StravaSport>
}