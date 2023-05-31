package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.civil31dot5.fitnessdiary.NavGraphDirections
import com.civil31dot5.fitnessdiary.databinding.FragmentBodyShapeRecordBinding
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.ui.utility.GenericRecordAdapterListener
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class BodyShapeRecordFragment : Fragment(), GenericRecordAdapterListener<BodyShapeRecord> {

    private var _binding: FragmentBodyShapeRecordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyShapeRecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodyShapeRecordBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        binding.rvBodyShape.adapter = BodyShapeRecordAdapter(this)
        binding.chart.apply {

            description = null

            axisRight.apply {
                axisMinimum = 5f
                axisMaximum = 40f
                setDrawGridLines(false)
            }
            axisLeft.apply {
                setDrawGridLines(false)
            }

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            setDrawBorders(true)
        }
    }

    private fun initListener() {
        binding.btAdd.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalAddBodyShapeRecordFragment())
        }
    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiStateFlow.collect { uiState ->
                        (binding.rvBodyShape.adapter as? BodyShapeRecordAdapter)?.submitList(uiState.bodyShapeRecord)
                        setChart(uiState.chartData)
                    }
                }
            }
        }

    }

    private fun setChart(chartData: List<BodyShapeRecordViewModel.BodyShapeChartData>) {
        if (chartData.isEmpty()) return

        val dateTimeIndex = mutableMapOf<Float, LocalDateTime>()
        val weightEntry = mutableListOf<Entry>()
        val fateRateEntry = mutableListOf<Entry>()

        chartData.mapIndexed { index, item ->
            dateTimeIndex[index.toFloat()] = item.dateTime
            weightEntry.add(Entry(index.toFloat(), item.weight.toFloat(), item))
            if (item.fatRate != null) {
                fateRateEntry.add(Entry(index.toFloat(), item.fatRate.toFloat(), item))
            }
        }

        val weightDataSet = LineDataSet(weightEntry, "Weight").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.RED
            circleColors = listOf(Color.RED)
            setDrawCircleHole(false)
            setDrawCircles(true)
            isHighlightEnabled = false
        }

        val fatRateDataSet = LineDataSet(fateRateEntry, "FatRate").apply {
            axisDependency = YAxis.AxisDependency.RIGHT
            color = Color.BLUE
            circleColors = listOf(Color.BLUE)
            setDrawCircleHole(false)
            setDrawCircles(true)
            isHighlightEnabled = false
        }

        val lineData = LineData(weightDataSet, fatRateDataSet)

        binding.chart.xAxis.apply {
            setLabelCount(dateTimeIndex.size, false)
            labelRotationAngle = 90f
            axisMinimum = -0.5f
            axisMaximum = weightDataSet.xMax + 0.5f
        }
        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val dateTime = dateTimeIndex[value] ?: return ""
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            }
        }

        binding.chart.data = lineData
        binding.chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditClick(data: BodyShapeRecord) {

    }

    override fun onDeleteClick(data: BodyShapeRecord) {
        AlertDialog.Builder(requireActivity())
            .setTitle("確定刪除?")
            .setPositiveButton("確定") { _, _ -> viewModel.deleteRecord(data) }
            .setNegativeButton("取消", null)
            .show()
    }
}