package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.databinding.FragmentAddBodyShapeRecordBinding
import com.civil31dot5.fitnessdiary.ui.base.BaseAddPhotoFragment
import com.civil31dot5.fitnessdiary.ui.base.SelectedPhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddBodyShapeRecordFragment : BaseAddPhotoFragment() {

    private val viewModel: AddBodyShapeRecordViewModel by viewModels()
    private var _binding: FragmentAddBodyShapeRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBodyShapeRecordBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        binding.rvPhotos.adapter = SelectedPhotosAdapter(
            onDeleteListener = {
                viewModel.deleteRecordImage(it)
            },
            onNoteChangedListener = { recordImage, note ->
                viewModel.onRecordImageNoteChanged(recordImage, note)
            }
        )
        binding.rvPhotos.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    private fun initListener() {
        binding.flDatePicker.setOnClickListener { showDatePicker() }
        binding.flTimePicker.setOnClickListener { showTimePicker() }
        binding.btAddPhoto.setOnClickListener { showSelectPhotoFromDialog() }
        binding.btCancel.setOnClickListener { findNavController().navigateUp() }
        binding.btConfirm.setOnClickListener {
            val note = binding.tlNote.editText?.text?.toString()
            val weight = binding.tlWeight.editText?.text.toString().toDoubleOrNull()
            val fatRate = binding.tlFat.editText?.text.toString().toDoubleOrNull()

            if (weight == null){
                AlertDialog.Builder(requireActivity())
                    .setTitle("請設定體重")
                    .setNegativeButton("OK", null)
                    .show()
                return@setOnClickListener
            }

            viewModel.submit(note, weight, fatRate)
        }

        binding.tlName.editText?.doOnTextChanged { text, start, before, count ->
            val text = binding.tlName.editText?.editableText?.toString() ?: return@doOnTextChanged
            viewModel.onRecordNameChanged(text)
        }

    }

    private fun bindViewModel() {
        this.addPhotoRecordViewModel = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.basicRecordData.collect {
                        with(binding) {
                            tlName.editText?.setText(it.name)
                            tlName.editText?.setSelection(it.name.length)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}