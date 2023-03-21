package com.civil31dot5.fitnessdiary.ui.record.diet

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.BitmapUtil
import com.civil31dot5.fitnessdiary.FileUtil
import com.civil31dot5.fitnessdiary.MyContentProvider
import com.civil31dot5.fitnessdiary.databinding.FragmentAddDietRecordBinding
import com.civil31dot5.fitnessdiary.ui.base.SelectedPhotosAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class AddDietRecordDialogFragment : Fragment() {

    private val viewModel: AddDietRecordViewModel by viewModels()
    private lateinit var binding: FragmentAddDietRecordBinding

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
                                viewModel.addPhoto(downScaleFile.absolutePath)
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
                                viewModel.addPhoto(downScaleFile.absolutePath)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDietRecordBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    private fun initView() {
        binding.rvPhotos.adapter = SelectedPhotosAdapter(
            onDeleteListener = {
                viewModel.deleteRecordImage(it)
            }
        )
        binding.rvPhotos.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }


    private fun initListener() {
        binding.flDatePicker.setOnClickListener { showDatePicker() }
        binding.flTimePicker.setOnClickListener { showTimePicker() }
        binding.btAddPhoto.setOnClickListener { showSelectPhotoFromDialog() }
        binding.btCancel.setOnClickListener { findNavController().navigateUp() }
        binding.btConfirm.setOnClickListener {
            val note = binding.tlNote.editText?.text?.toString()
            viewModel.submit(note)
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.basicRecordData.collect {
                        with(binding) {
                            tlName.editText?.setText(it.name)
                            tlDate.editText?.setText(it.date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                            tlTime.editText?.setText(it.time.format(DateTimeFormatter.ofPattern("HH:mm")))
                            ilLoading.root.isVisible = it.isLoading
                            if (it.addRecordSuccess == true) {
                                Toast.makeText(requireContext(), "新增成功", Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                        }
                    }
                }

                launch {
                    viewModel.photoRecordData.collect {
                        (binding.rvPhotos.adapter as? SelectedPhotosAdapter)?.submitList(it.selectedPhotos)
                        binding.btAddPhoto.isVisible = it.isAddButtonVisible

                    }
                }
            }
        }
    }

    private fun showDatePicker() {
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
            viewModel.setDate(localDate)
        }

        datePicker.show(childFragmentManager, "date_picker")
    }

    private fun showTimePicker() {
        val timerPicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setPositiveButtonText("Ok")
            .setNegativeButtonText("Cancel")
            .build()

        timerPicker.addOnPositiveButtonClickListener {
            val hour = timerPicker.hour
            val min = timerPicker.minute
            val localTime = LocalTime.of(hour, min)
            viewModel.setTime(localTime)
        }

        timerPicker.show(childFragmentManager, "time_picker")
    }

    private fun showSelectPhotoFromDialog() {
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