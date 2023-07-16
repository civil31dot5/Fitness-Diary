package com.civil31dot5.fitnessdiary.testing.repository

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class TestStravaRepository: StravaRepository {

    private val testData = MutableStateFlow<List<StravaSportRecord>>(emptyList())
    override fun getSportRecord(from: LocalDate, to: LocalDate): Flow<List<StravaSportRecord>> {
        return testData
    }

    fun setStravaRecordData(data: List<StravaSportRecord>){
        testData.update { data }
    }

    override suspend fun syncSportRecord(from: LocalDate, to: LocalDate) {

    }
}