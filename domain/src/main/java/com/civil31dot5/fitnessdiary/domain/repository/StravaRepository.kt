package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.StravaSport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StravaRepository {
    fun getSportHistory(from: LocalDate, to: LocalDate): Flow<List<StravaSport>>

    suspend fun syncSportHistory(from: LocalDate, to: LocalDate)
}