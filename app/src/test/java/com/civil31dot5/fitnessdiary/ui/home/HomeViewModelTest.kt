package com.civil31dot5.fitnessdiary.ui.home

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.domain.usecase.GetMonthRecordStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetMonthDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetMonthStravaSportRecordUseCase
import com.civil31dot5.fitnessdiary.testing.repository.TestRecordRepository
import com.civil31dot5.fitnessdiary.testing.repository.TestStravaRepository
import com.civil31dot5.fitnessdiary.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRecordRepository = TestRecordRepository()
    private val testStravaRepository = TestStravaRepository()

    private val getMonthDietRecordUseCase = GetMonthDietRecordUseCase(testRecordRepository)
    private val getMonthStravaSportRecordUseCase =
        GetMonthStravaSportRecordUseCase(testStravaRepository)

    private val getMonthRecordStatusUseCase =
        GetMonthRecordStatusUseCase(
            getMonthDietRecordUseCase,
            getMonthStravaSportRecordUseCase
        )

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(getMonthRecordStatusUseCase)
    }

    @Test
    fun uiState_dietAndSportRecordAtSameDate() = runTest {

        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }

        val targetDate = LocalDate.of(2023,7,16)

        testRecordRepository.setDietRecordTestData(
            listOf(
                DietRecord(
                    id = "dietRecordTestId1",
                    name = "飲食紀錄",
                    dateTime = LocalDateTime.of(
                        targetDate,
                        LocalTime.of(18, 0)
                    ),
                    note = "備註(DietRecord)",
                    images = listOf(
                        RecordImage(
                            id = "dietRecordTestId1_ImageId1",
                            filePath = "images/dietRecordTestId1_ImageId1.jpg",
                            note = "備註(DietImage)"
                        )
                    )
                )
            )
        )

        testStravaRepository.setStravaRecordData(
            listOf(
                StravaSportRecord(
                    stravaId = 1L,
                    dateTime = LocalDateTime.of(
                        targetDate,
                        LocalTime.of(18, 0)
                    ),
                    name = "2023/7/16 RUNNING",
                    distance = 1000.0,
                    calories = 500.0,
                    type = "RUNNING",
                    elapsedTimeSec = 3600
                ),
            )
        )

        viewModel.selectYearMonth(YearMonth.now())

        val uiState = viewModel.uiState.value

        assertTrue(uiState.recordStatus.containsKey(targetDate))

        val recordStatue = uiState.recordStatus[targetDate]

        assertTrue(recordStatue!!.hasSportHistory)
        assertTrue(recordStatue.hasDietRecord)

        collectJob.cancel()
    }
}