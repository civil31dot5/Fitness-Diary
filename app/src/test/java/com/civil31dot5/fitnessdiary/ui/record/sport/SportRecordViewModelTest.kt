package com.civil31dot5.fitnessdiary.ui.record.sport

import com.civil31dot5.fitnessdiary.domain.model.StravaConnectStatus
import com.civil31dot5.fitnessdiary.domain.usecase.sport.ConnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.DisconnectStravaUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaConnectStatusUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.sport.GetStravaSportRecordUseCase
import com.civil31dot5.fitnessdiary.testing.TestStravaAccountManager
import com.civil31dot5.fitnessdiary.testing.data.stravaSportRecordTestData
import com.civil31dot5.fitnessdiary.testing.repository.TestStravaRepository
import com.civil31dot5.fitnessdiary.testing.util.MainDispatcherRule
import com.civil31dot5.fitnessdiary.ui.record.diet.DietRecordHistoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SportRecordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testStravaRepository = TestStravaRepository()
    private val testStravaAccountManager = TestStravaAccountManager()

    private val getStravaConnectStatusUseCase = GetStravaConnectStatusUseCase(testStravaAccountManager)
    private val connectStravaUseCase = ConnectStravaUseCase(testStravaAccountManager)
    private val disconnectStravaUseCase = DisconnectStravaUseCase(testStravaAccountManager)
    private val getStravaSportRecordUseCase = GetStravaSportRecordUseCase(testStravaRepository)

    private lateinit var viewModel: SportRecordViewModel

    @Before
    fun setUp() {
        viewModel = SportRecordViewModel(
            getStravaConnectStatusUseCase,
            connectStravaUseCase,
            disconnectStravaUseCase,
            getStravaSportRecordUseCase
        )
    }

    @Test
    fun uiState_whenConnectStrava_true() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }

        testStravaAccountManager.setStravaConnectStatus(StravaConnectStatus.NotConnected)

        assertEquals(
            false,
            viewModel.uiState.value.hasConnectStrava
        )

        testStravaAccountManager.setStravaConnectStatus(StravaConnectStatus.Connected)
        viewModel.connectStrava()

        assertEquals(
            true,
            testStravaAccountManager.isConnectStravaCalled
        )

        assertEquals(
            true,
            viewModel.uiState.value.hasConnectStrava
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenLoadSportHistory() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }

        testStravaAccountManager.setStravaConnectStatus(StravaConnectStatus.Connected)

        val testData = stravaSportRecordTestData[0]

        testStravaRepository.setStravaRecordData(listOf(testData))

        viewModel.refreshConnectStatus()

        assertEquals(
            testData,
            viewModel.uiState.value.sportHistory[0]
        )

        collectJob.cancel()
    }

}