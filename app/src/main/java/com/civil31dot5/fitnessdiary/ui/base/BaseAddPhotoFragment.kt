package com.civil31dot5.fitnessdiary.ui.base

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.civil31dot5.fitnessdiary.BitmapUtil
import com.civil31dot5.fitnessdiary.FileUtil
import com.civil31dot5.fitnessdiary.MyContentProvider
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.extraFile
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class BaseAddPhotoContentState {

    var showSelectPhotoFromDialog by mutableStateOf(false)
    var showDatePicker by mutableStateOf(false)
    var showTimePicker by mutableStateOf(false)
    var tryTakePhoto by mutableStateOf(false)

    fun showSelectPhotoFromDialog(){
        showSelectPhotoFromDialog = true
    }

    fun showDatePicker(){
        showDatePicker = true
    }

    fun showTimePicker(){
        showTimePicker = true
    }

    fun tryTakePhoto(){
        tryTakePhoto = true
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BaseAddPhotoContent(
    state: BaseAddPhotoContentState,
    onAddPhoto: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var filePath: String? by remember { mutableStateOf(null) }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            var processSuccess = false
            if (success && filePath != null) {
                try {
                    File(filePath!!).inputStream()
                        .use { inputStream ->
                            val downScaleFile = FileUtil.createTempJpgFile(context)
                            val scaleSuccess =
                                BitmapUtil.inSampleSizeToFile(inputStream, downScaleFile)
                            if (scaleSuccess) {
                                onAddPhoto(downScaleFile.absolutePath)
                                state.tryTakePhoto = false
                                processSuccess = true
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            filePath = null
            if (!processSuccess) {
                Toast.makeText(context, "新增照片失敗", Toast.LENGTH_LONG).show()
            }
        }
    )

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {imageUri ->
            var processSuccess = false
            if (imageUri != null) {
                try {
                    context.contentResolver.openInputStream(imageUri)
                        ?.use { inputStream ->
                            val downScaleFile = FileUtil.createTempJpgFile(context)
                            val scaleSuccess =
                                BitmapUtil.inSampleSizeToFile(inputStream, downScaleFile)
                            if (scaleSuccess) {
                                onAddPhoto(downScaleFile.absolutePath)
                                processSuccess = true
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (!processSuccess) {
                Toast.makeText(context, "新增照片失敗", Toast.LENGTH_LONG).show()
            }
        }
    )

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted && state.tryTakePhoto){
        val tmpPhotoFile = FileUtil.createTempJpgFile(context)

        filePath = tmpPhotoFile.absolutePath

        val uri = MyContentProvider.getContentUri(context, tmpPhotoFile)

        takePhotoLauncher.launch(uri)
    }

    content()

    if (state.showSelectPhotoFromDialog) {
        SelectPhotoFromDialog(
            onDismissRequest = { state.showSelectPhotoFromDialog = false },
            onTakePhotoClick = {
                state.tryTakePhoto()
                if (!cameraPermissionState.status.isGranted){
                    cameraPermissionState.launchPermissionRequest()
                }
            },
            onPickPhotoClick = {
                pickPhotoLauncher.launch("image/*")
            }
        )
    }

    if (state.showDatePicker){
        DatePickerDialog(
            onDismissRequest = { state.showDatePicker = false},
            onDateSelected = onDateSelected
        )
    }

    if (state.showTimePicker){
        TimePickerDialog(
            onDismissRequest = { state.showTimePicker = false},
            onTimeSelected = onTimeSelected
        )
    }
}


@Composable
fun SelectPhotoFromDialog(
    onDismissRequest: () -> Unit = {},
    onTakePhotoClick: () -> Unit = {},
    onPickPhotoClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "從...選取照片")
                Spacer(modifier = Modifier.height(24.dp))

                Divider()

                TextButton(onClick = {
                    onTakePhotoClick()
                    onDismissRequest()
                }) {
                    Text(text = "拍照")
                }

                TextButton(onClick = {
                    onPickPhotoClick()
                    onDismissRequest()
                }) {
                    Text(text = "相簿")
                }

                TextButton(onClick = onDismissRequest) {
                    Text(text = "取消")
                }

            }
        }

    }
}

@Preview
@Composable
fun PreviewSelectPhotoFromDialog() {
    FitnessDiaryTheme {
        SelectPhotoFromDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = MaterialDatePicker.todayInUtcMilliseconds()
    )
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val localDate = Instant
                        .ofEpochMilli(datePickerState.selectedDateMillis ?: return@TextButton)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    onDateSelected(localDate)
                    onDismissRequest()
                },
                enabled = confirmEnabled.value
            ) {
                Text("確認")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
fun PreviewDatePickerCompose() {
    FitnessDiaryTheme {
        DatePickerDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit = {},
    onTimeSelected: (LocalTime) -> Unit = {}
) {
    val timeNow = LocalTime.now()
    val timePickerState = rememberTimePickerState(
        initialHour = timeNow.hour,
        initialMinute = timeNow.minute,
        is24Hour = false
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val localTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    onTimeSelected(localTime)
                    onDismissRequest()
                }
            ) {
                Text("確認")
            }
        }
    ) {
        TimePicker(
            state = timePickerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun PreviewTimePickerCompose() {
    FitnessDiaryTheme {
        TimePickerDialog()
    }
}

@Composable
fun InputFieldName(
    modifier: Modifier = Modifier,
    name: String = "name",
    onNameUpdate: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameUpdate,
        label = { Text(text = "名稱") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_food_bank),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

@Composable
fun InputFieldDate(
    modifier: Modifier = Modifier,
    dateString: String = "",
    onClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = dateString,
        onValueChange = {},
        enabled = false,
        label = { Text(text = "日期") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_calendar),
                contentDescription = null
            )
        },
        modifier = Modifier
            .clickable { onClick() }
            .then(modifier)
    )
}

@Composable
fun InputFieldTime(
    modifier: Modifier = Modifier,
    timeString: () -> String = {""},
    onClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = timeString(),
        onValueChange = {},
        enabled = false,
        label = { Text(text = "時間") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = null
            )
        },
        modifier = Modifier
            .clickable { onClick() }
            .then(modifier)
    )
}

@Composable
fun InputFieldNote(
    modifier: Modifier = Modifier,
    note: String = "",
    onNoteUpdate: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = note,
        onValueChange = onNoteUpdate,
        label = { Text(text = "備註") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_note),
                contentDescription = null
            )
        },
        modifier = modifier
    )
}

@Composable
fun SelectedImageItem(
    photo: RecordImage,
    onImageNoteChanged: (RecordImage, String) -> Unit,
    onDeleteClicked: (RecordImage) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.extraFile(LocalContext.current))
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = photo.note,
            onValueChange = { note ->
                onImageNoteChanged(photo, note)
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onDeleteClicked(photo) }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
        }

    }
}