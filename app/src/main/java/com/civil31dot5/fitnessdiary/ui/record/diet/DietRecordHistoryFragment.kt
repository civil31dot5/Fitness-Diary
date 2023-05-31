package com.civil31dot5.fitnessdiary.ui.record.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.civil31dot5.fitnessdiary.databinding.FragmentDietRecordHistoryBinding
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DietRecordHistoryFragment : Fragment() {

    private var _binding: FragmentDietRecordHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DietRecordHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietRecordHistoryBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        binding.rvDietHistory.adapter = DietRecordAdapter(
            onEditClick = { Snackbar.make(requireView(), "WIP", Snackbar.LENGTH_LONG).show() },
            onDeleteClick = { showConfirmDeleteDialog(it) }
        )
        binding.rvDietHistory.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
    }

    private fun showConfirmDeleteDialog(dietRecord: DietRecord) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("確認刪除?")
            .setPositiveButton("確認") { _, _ ->
                viewModel.deleteDietRecord(dietRecord)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun initListener() {

    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dietRecordList.collect {
                    (binding.rvDietHistory.adapter as? DietRecordAdapter)?.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}