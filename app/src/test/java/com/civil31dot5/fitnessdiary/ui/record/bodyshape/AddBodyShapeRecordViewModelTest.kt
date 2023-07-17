package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.AddBodyShapeRecordUseCase
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
class AddBodyShapeRecordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRecordRepository = TestRecordRepository()

    private val addBodyShapeRecordUseCase = AddBodyShapeRecordUseCase(testRecordRepository)

    private lateinit var viewModel: AddBodyShapeRecordViewModel

    @Before
    fun setUp() {
        viewModel = AddBodyShapeRecordViewModel(
            addBodyShapeRecordUseCase
        )
    }

    @Test
    fun updateWeightCorrect() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.weightFlow.collect{} }

        val testWeight = "75.7"

        viewModel.updateWeight(testWeight)

        assertEquals(
            testWeight,
            viewModel.weightFlow.value
        )

        collectJob.cancel()
    }

    @Test
    fun updateFatRateCorrect() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.fatRateFlow.collect{} }

        val testFatRate = "19.7"

        viewModel.updateFatRate(testFatRate)

        assertEquals(
            testFatRate,
            viewModel.fatRateFlow.value
        )
        collectJob.cancel()
    }

    @Test
    fun addBodyShapeRecordCorrect() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) {
            launch { viewModel.basicRecordData.collect{}  }
            launch { viewModel.photoRecordData.collect{}  }
            launch { viewModel.weightFlow.collect{}  }
            launch { viewModel.fatRateFlow.collect{}  }
        }

        val testData = bodyShapeRecordTestData[0]

        viewModel.setName(testData.name)
        viewModel.setDate(testData.dateTime.toLocalDate())
        viewModel.setTime(testData.dateTime.toLocalTime())
        viewModel.setNote(testData.note)
        viewModel.updateWeight(testData.weight.toString())
        viewModel.updateFatRate(testData.bodyFatPercentage.toString())
        viewModel.addPhoto(testData.images[0].filePath)

        viewModel.submit()

        assertEquals(
            testData.name,
            testRecordRepository.recentAddBodyShapeRecord!!.name
        )

        assertEquals(
            testData.dateTime,
            testRecordRepository.recentAddBodyShapeRecord!!.dateTime
        )

        assertEquals(
            testData.note,
            testRecordRepository.recentAddBodyShapeRecord!!.note
        )

        assertEquals(
            testData.weight,
            testRecordRepository.recentAddBodyShapeRecord!!.weight,
            0.01
        )

        assertEquals(
            testData.images[0].filePath,
            testRecordRepository.recentAddBodyShapeRecord!!.images[0].filePath
        )

        assertEquals(
            testData.bodyFatPercentage!!,
            testRecordRepository.recentAddBodyShapeRecord!!.bodyFatPercentage!!,
            0.01
        )

        assertTrue(viewModel.basicRecordData.value.addRecordSuccess!!)

        collectJob.cancel()
    }

}