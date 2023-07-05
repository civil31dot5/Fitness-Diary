package com.civil31dot5.fitnessdiary.ui.base

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.civil31dot5.fitnessdiary.BitmapUtil
import com.civil31dot5.fitnessdiary.FileUtil
import com.civil31dot5.fitnessdiary.MyContentProvider
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

open class BaseAddPhotoFragment : Fragment() {

    protected var addPhotoRecordViewModel: AddPhotoRecordViewModel? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                tryTakePhoto()
            } else {
                Toast.makeText(requireContext(), "未取得權限", Toast.LENGTH_LONG).show()
            }
        }

    private var filePath: String? = null

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            var processSuccess = false
            if (success && filePath != null) {
                try {
                    File(filePath!!).inputStream()
                        .use { inputStream ->
                            val downScaleFile = FileUtil.createTempJpgFile(requireContext())
                            val scaleSuccess =
                                BitmapUtil.inSampleSizeToFile(inputStream, downScaleFile)
                            if (scaleSuccess) {
                                addPhotoRecordViewModel?.addPhoto(downScaleFile.absolutePath)
                                processSuccess = true
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            filePath = null
            if (!processSuccess) {
                Toast.makeText(requireContext(), "新增照片失敗", Toast.LENGTH_LONG).show()
            }
        }

    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            var processSuccess = false
            if (imageUri != null) {
                try {
                    requireContext().contentResolver.openInputStream(imageUri)
                        ?.use { inputStream ->
                            val downScaleFile = FileUtil.createTempJpgFile(requireContext())
                            val scaleSuccess =
                                BitmapUtil.inSampleSizeToFile(inputStream, downScaleFile)
                            if (scaleSuccess) {
                                addPhotoRecordViewModel?.addPhoto(downScaleFile.absolutePath)
                                processSuccess = true
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (!processSuccess) {
                Toast.makeText(requireContext(), "新增照片失敗", Toast.LENGTH_LONG).show()
            }
        }


    protected fun showDatePicker() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setPositiveButtonText("Ok")
            .setNegativeButtonText("Cancel")
            .setCalendarConstraints(calendarConstraints)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            addPhotoRecordViewModel?.setDate(localDate)
        }

        datePicker.show(childFragmentManager, "date_picker")
    }

    protected fun showTimePicker() {
        val timeNow = LocalTime.now()
        val timerPicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setHour(timeNow.hour)
            .setMinute(timeNow.minute)
            .setPositiveButtonText("Ok")
            .setNegativeButtonText("Cancel")
            .build()

        timerPicker.addOnPositiveButtonClickListener {
            val hour = timerPicker.hour
            val min = timerPicker.minute
            val localTime = LocalTime.of(hour, min)
            addPhotoRecordViewModel?.setTime(localTime)
        }

        timerPicker.show(childFragmentManager, "time_picker")
    }

    protected fun showSelectPhotoFromDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("從..選取照片")
            .setItems(arrayOf("拍照", "相簿", "取消")) { dialog, which ->
                when (which) {
                    0 -> tryTakePhoto()
                    1 -> pickPhotoLauncher.launch("image/*")
                    else -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun tryTakePhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
            return
        }

        val tmpPhotoFile = FileUtil.createTempJpgFile(requireContext())

        filePath = tmpPhotoFile.absolutePath

        val uri = MyContentProvider.getContentUri(requireContext(), tmpPhotoFile)

        takePhotoLauncher.launch(uri)
    }

}

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