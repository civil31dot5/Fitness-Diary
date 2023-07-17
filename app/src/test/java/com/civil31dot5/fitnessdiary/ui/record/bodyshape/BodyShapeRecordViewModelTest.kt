package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.DeleteBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.GetAllBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.testing.data.bodyShapeRecordTestData
import com.civil31dot5.fitnessdiary.testing.repository.TestRecordRepository
import com.civil31dot5.fitnessdiary.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BodyShapeRecordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRecordRepository = TestRecordRepository()

    private val getAllBodyShapeRecordUseCase = GetAllBodyShapeRecordUseCase(testRecordRepository)

    private val deleteBodyShapeRecordUseCase = DeleteBodyShapeRecordUseCase(testRecordRepository)

    private lateinit var viewModel: BodyShapeRecordViewModel

    @Before
    fun setUp() {
        viewModel = BodyShapeRecordViewModel(
            getAllBodyShapeRecordUseCase,
            deleteBodyShapeRecordUseCase
        )
    }

    @Test
    fun uiState_receive_data() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiStateFlow.collect{} }

        val testData = listOf(bodyShapeRecordTestData[0])

        testRecordRepository.setBodyShapeRecordTestData(testData)

        val uiState = viewModel.uiStateFlow.value

        assertArrayEquals(
            testData.toTypedArray(),
            uiState.bodyShapeRecord.toTypedArray()
        )

        val chartData = uiState.chartData[0]

        assertEquals(
            testData[0].dateTime,
            chartData.dateTime,
        )

        assertEquals(
            testData[0].weight,
            chartData.weight,
            0.01
        )

        assertEquals(
            testData[0].bodyFatPercentage!!,
            chartData.fatRate!!,
            0.01
        )

        collectJob.cancel()
    }

    @Test
    fun deleteBodyShapeRecord_Repository_ReceiveCorrectData() = runTest {

        viewModel.deleteRecord(bodyShapeRecordTestData[0])

        assertEquals(
            bodyShapeRecordTestData[0],
            testRecordRepository.recentDeleteBodyShapeRecord

        )
    }
}