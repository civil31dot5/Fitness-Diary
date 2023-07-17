package com.civil31dot5.fitnessdiary.ui.record.diet

import com.civil31dot5.fitnessdiary.domain.usecase.diet.DeleteDietRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.diet.GetAllDietRecordUseCase
import com.civil31dot5.fitnessdiary.testing.data.dietRecordTestData
import com.civil31dot5.fitnessdiary.testing.repository.TestRecordRepository
import com.civil31dot5.fitnessdiary.testing.util.MainDispatcherRule
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.BodyShapeRecordViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DietRecordHistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRecordRepository = TestRecordRepository()

    private val getAllDietRecordUseCase = GetAllDietRecordUseCase(testRecordRepository)

    private val deleteDietRecordUseCase = DeleteDietRecordUseCase(testRecordRepository)

    private lateinit var viewModel: DietRecordHistoryViewModel

    @Before
    fun setUp() {
        viewModel = DietRecordHistoryViewModel(
            getAllDietRecordUseCase,
            deleteDietRecordUseCase
        )
    }

    @Test
    fun uiState_receive_data() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.dietRecordList.collect{} }

        val testData = dietRecordTestData[0]

        testRecordRepository.setDietRecordTestData(listOf(testData))

        val receivedData = viewModel.dietRecordList.value[0]

        assertEquals(
            testData,
            receivedData
        )

        collectJob.cancel()
    }

    @Test
    fun deleteDietRecord_Repository_ReceiveCorrectData() = runTest{
        val testData = dietRecordTestData[0]

        viewModel.deleteDietRecord(testData)

        assertEquals(
            testData,
            testRecordRepository.recentDeleteDietRecord
        )
    }

}