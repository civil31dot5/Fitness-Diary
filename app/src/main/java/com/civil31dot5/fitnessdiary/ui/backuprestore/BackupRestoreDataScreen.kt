package com.civil31dot5.fitnessdiary.ui.backuprestore

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
                Toast.makeText(context, "未選擇檔案儲存位置", Toast.LENGTH_LONG).show()
                return@rememberLauncherForActivityResult
            }
            viewModel.backupData(uri.toString())
        }
    )

    val selectRestoreFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri == null) {
                Toast.makeText(context, "未選擇檔案", Toast.LENGTH_LONG).show()
                return@rememberLauncherForActivityResult
            }
            viewModel.restoreData(uri.toString())
        }
    )

    BackupRestoreContent(
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
}

@Composable
fun BackupRestoreContent(
    onBackupDataClick: () -> Unit,
    onRestoreDataClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = onBackupDataClick) {
            Icon(
                Icons.Outlined.FileDownload,
                contentDescription = "Backup data",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "備份資料")
        }

        Button(onClick = onRestoreDataClick) {
            Icon(
                Icons.Outlined.ImportExport,
                contentDescription = "Restore data",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "還原資料")
        }
    }
}