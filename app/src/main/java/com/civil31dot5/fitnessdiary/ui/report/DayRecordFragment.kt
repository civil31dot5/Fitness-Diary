package com.civil31dot5.fitnessdiary.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.civil31dot5.fitnessdiary.databinding.FragmentDayRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DayRecordFragment : Fragment() {

    private var _binding: FragmentDayRecordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DayRecordViewModel by viewModels()

    private val arg: DayRecordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayRecordBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        with(binding) {
            tvDate.text = arg.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            rvRecords.adapter = DayRecordAdapter()
            rvRecords.setHasFixedSize(true)
        }
    }

    private fun initListener() {

    }

    private fun bindViewModel() {
        viewModel.setDate(arg.date)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDateRecords.collect {
                    (binding.rvRecords.adapter as? DayRecordAdapter)?.submitList(it)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}