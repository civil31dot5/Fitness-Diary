package com.civil31dot5.fitnessdiary.ui.base

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.civil31dot5.fitnessdiary.BitmapUtil
import com.civil31dot5.fitnessdiary.FileUtil
import com.civil31dot5.fitnessdiary.MyContentProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.io.File
import java.time.Instant
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