package com.civil31dot5.fitnessdiary.ui.backuprestore

import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupDataUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.BackupStatus
import com.civil31dot5.fitnessdiary.domain.usecase.backuprestore.RestoreDataUseCase
import com.civil31dot5.fitnessdiary.testing.repository.TestBackupRestoreDataRepository
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
class BackupRestoreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testBackupRestoreDataRepository = TestBackupRestoreDataRepository()

    private val backupDataUseCase = BackupDataUseCase(testBackupRestoreDataRepository)

    private val restoreUseCase = RestoreDataUseCase(testBackupRestoreDataRepository)

    private lateinit var viewModel: BackupRestoreViewModel

    private val testFilePath = "test_path"
    @Before
    fun setUp() {
        viewModel = BackupRestoreViewModel(
            backupDataUseCase,
            restoreUseCase
        )
    }

    @Test
    fun uiState_initStatus_NoTaskRunning() = runTest {
        assertEquals(
            BackupRestoreViewModel.UiState.NoTaskRunning,
            viewModel.uiState.value
        )
    }

    @Test
    fun uiState_whenBackup_Success() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }
        viewModel.backupData(testFilePath)

        assertEquals(
            testFilePath,
            testBackupRestoreDataRepository.receivedBackupPath
        )

        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.InProgress(0))
        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.InProgress(10))
        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.InProgress(50))

        assertEquals(
            BackupRestoreViewModel.UiState.TaskRunning,
            viewModel.uiState.value
        )

        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.Success)

        assertEquals(
            BackupRestoreViewModel.UiState.BackupDataTaskSuccess,
            viewModel.uiState.value
        )

        viewModel.messageShown()

        assertEquals(
            BackupRestoreViewModel.UiState.NoTaskRunning,
            viewModel.uiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenBackup_Fail() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }
        viewModel.backupData(testFilePath)

        assertEquals(
            testFilePath,
            testBackupRestoreDataRepository.receivedBackupPath
        )

        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.InProgress(0))

        assertEquals(
            BackupRestoreViewModel.UiState.TaskRunning,
            viewModel.uiState.value
        )

        testBackupRestoreDataRepository.setBackupStatus(BackupStatus.Fail)

        assertEquals(
            BackupRestoreViewModel.UiState.TaskFail,
            viewModel.uiState.value
        )

        viewModel.messageShown()

        assertEquals(
            BackupRestoreViewModel.UiState.NoTaskRunning,
            viewModel.uiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenRestore_Success() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }

        viewModel.restoreData(testFilePath)

        assertEquals(
            testFilePath,
            testBackupRestoreDataRepository.receivedRestorePath
        )

        assertEquals(
            BackupRestoreViewModel.UiState.RestoreDataTaskSuccess,
            viewModel.uiState.value
        )

        viewModel.messageShown()

        assertEquals(
            BackupRestoreViewModel.UiState.NoTaskRunning,
            viewModel.uiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun uiState_whenRestore_Fail() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect{} }

        testBackupRestoreDataRepository.isForceRestoreFail = true
        viewModel.restoreData(testFilePath)

        assertEquals(
            testFilePath,
            testBackupRestoreDataRepository.receivedRestorePath
        )

        assertEquals(
            BackupRestoreViewModel.UiState.TaskFail,
            viewModel.uiState.value
        )

        viewModel.messageShown()

        assertEquals(
            BackupRestoreViewModel.UiState.NoTaskRunning,
            viewModel.uiState.value
        )

        collectJob.cancel()
    }


}