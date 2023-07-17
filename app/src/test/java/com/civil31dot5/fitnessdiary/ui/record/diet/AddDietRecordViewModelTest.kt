package com.civil31dot5.fitnessdiary.ui.record.diet

import com.civil31dot5.fitnessdiary.domain.usecase.diet.AddDietRecordUseCase
import com.civil31dot5.fitnessdiary.testing.data.dietRecordTestData
import com.civil31dot5.fitnessdiary.testing.repository.TestRecordRepository
import com.civil31dot5.fitnessdiary.testing.util.MainDispatcherRule
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.AddBodyShapeRecordViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddDietRecordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testRecordRepository = TestRecordRepository()

    private val addDietRecordUseCase = AddDietRecordUseCase(testRecordRepository)

    private lateinit var viewModel: AddDietRecordViewModel
    @Before
    fun setUp() {
        viewModel = AddDietRecordViewModel(
            addDietRecordUseCase
        )
    }

    @Test
    fun addDietRecordCorrect() = runTest {

        val testData = dietRecordTestData[0]

        viewModel.setName(testData.name)
        viewModel.setDate(testData.dateTime.toLocalDate())
        viewModel.setTime(testData.dateTime.toLocalTime())
        viewModel.setNote(testData.note)
        viewModel.addPhoto(testData.images[0].filePath)

        viewModel.submit()

        assertEquals(
            testData.name,
            testRecordRepository.recentAddDietRecord!!.name
        )

        assertEquals(
            testData.dateTime,
            testRecordRepository.recentAddDietRecord!!.dateTime
        )

        assertEquals(
            testData.note,
            testRecordRepository.recentAddDietRecord!!.note
        )

        assertEquals(
            testData.images[0].filePath,
            testRecordRepository.recentAddDietRecord!!.images[0].filePath
        )

        assertTrue(viewModel.basicRecordData.value.addRecordSuccess!!)

    }
}