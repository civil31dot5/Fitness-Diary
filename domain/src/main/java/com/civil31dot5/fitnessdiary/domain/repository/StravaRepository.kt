package com.civil31dot5.fitnessdiary.domain.repository

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StravaRepository {
    fun getSportRecord(from: LocalDate, to: LocalDate): Flow<List<StravaSportRecord>>

    suspend fun syncSportRecord(from: LocalDate, to: LocalDate)
}