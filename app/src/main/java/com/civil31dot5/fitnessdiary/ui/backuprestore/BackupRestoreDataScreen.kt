package com.civil31dot5.fitnessdiary.ui.backuprestore

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.ui.utility.getActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun BackupRestoreDataRoute(
    viewModel: BackupRestoreViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectBackupFilePositionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
        onResult = { uri ->
            if (uri == null) {
                Toast.makeText(
                    context,
                    ContextCompat.getString(context, R.string.not_select_filepath),
                    Toast.LENGTH_LONG
                ).show()
                return@rememberLauncherForActivityResult
            }
            viewModel.backupData(uri.toString())
        }
    )

    val selectRestoreFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri == null) {
                Toast.makeText(
                    context,
                    ContextCompat.getString(context, R.string.not_select_file), Toast.LENGTH_LONG
                ).show()
                return@rememberLauncherForActivityResult
            }
            viewModel.restoreData(uri.toString())
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackupRestoreContent(
        isButtonEnable = uiState == BackupRestoreViewModel.UiState.NoTaskRunning,
        isShowProgressBar = uiState == BackupRestoreViewModel.UiState.TaskRunning,
        onBackupDataClick = {
            val dateTimeString =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"))
            val fileName = "FitnessDiary-$dateTimeString"
            selectBackupFilePositionLauncher.launch(fileName)
        },
        onRestoreDataClick = {
            selectRestoreFileLauncher.launch(arrayOf("application/zip"))
        }
    )


    when (uiState) {
        BackupRestoreViewModel.UiState.BackupDataTaskSuccess -> {
            Toast.makeText(context, stringResource(R.string.backup_data_success), Toast.LENGTH_LONG).show()
            viewModel.messageShown()
        }

        BackupRestoreViewModel.UiState.RestoreDataTaskSuccess -> {
            Toast.makeText(context, stringResource(R.string.restore_data_success), Toast.LENGTH_LONG).show()
            context.getActivity()?.run { finish() }
        }

        BackupRestoreViewModel.UiState.TaskFail -> {
            Toast.makeText(context, stringResource(R.string.task_fail_try_again), Toast.LENGTH_LONG).show()
            viewModel.messageShown()
        }

        else -> {}
    }
}

@Composable
fun BackupRestoreContent(
    isButtonEnable: Boolean = true,
    isShowProgressBar: Boolean = false,
    onBackupDataClick: () -> Unit,
    onRestoreDataClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        if (isShowProgressBar) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Button(
                enabled = isButtonEnable,
                onClick = onBackupDataClick
            ) {
                Icon(
                    Icons.Outlined.FileDownload,
                    contentDescription = "Backup data",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.backup_data))
            }

            Button(
                enabled = isButtonEnable,
                onClick = onRestoreDataClick
            ) {
                Icon(
                    Icons.Outlined.ImportExport,
                    contentDescription = "Restore data",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.restore_data))
            }
        }
    }

}