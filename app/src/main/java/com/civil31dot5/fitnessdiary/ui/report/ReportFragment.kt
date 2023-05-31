package com.civil31dot5.fitnessdiary.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civil31dot5.fitnessdiary.databinding.FragmentReportBinding
import com.civil31dot5.fitnessdiary.domain.model.WeekReport
import com.civil31dot5.fitnessdiary.ui.utility.FixedLabelXAxisRender
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(layoutInflater)
        initView()
        initListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun initView() {
        binding.chartWeightCalories.apply {
            description = null
            axisRight.apply {
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 20f
                axisMaximum = 130f
            }

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = 90f
                axisMinimum = -0.5f
            }
            setDrawBorders(true)
            extraBottomOffset = 60f
            isHighlightPerTapEnabled = false
            isHighlightPerDragEnabled = false
        }

        binding.chartFatRateCalories.apply {
            description = null

            axisRight.apply {
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 5f
                axisMaximum = 40f
            }

            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = 90f
                axisMinimum = -0.5f
            }
            setDrawBorders(true)
            extraBottomOffset = 60f
            isHighlightPerTapEnabled = false
            isHighlightPerDragEnabled = false
        }

    }

    private fun initListener() {

    }

    private fun bindViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weekReport.collect {
                    setupChart(it.sortedBy { it.yearWeek })
                }
            }
        }
    }

    private fun setupChart(weekReposts: List<WeekReport>) {

        val xLabelMap = mutableMapOf<Float, WeekReport>()

        weekReposts.forEachIndexed { index, weekReport ->
            xLabelMap.put(index.toFloat(), weekReport)
        }

        val xLabelPosition = List(weekReposts.size) { index -> index.toFloat() }.toFloatArray()

        val caloriesEntry = weekReposts.mapIndexed { index, weekReport ->
            BarEntry(index.toFloat(), weekReport.weekSportCalories.toFloat(), weekReport)
        }

        val weightEntry = weekReposts.mapIndexedNotNull { index, weekReport ->
            weekReport.weekWeight ?: return@mapIndexedNotNull null
            Entry(index.toFloat(), weekReport.weekWeight!!.toFloat(), weekReport)
        }

        val fatRateEntry = weekReposts.mapIndexedNotNull { index, weekReport ->
            weekReport.weekFatRate ?: return@mapIndexedNotNull null
            Entry(index.toFloat(), weekReport.weekFatRate!!.toFloat(), weekReport)
        }

        val caloriesDataSetForWeight = BarDataSet(caloriesEntry, "運動卡路里").apply {
            axisDependency = YAxis.AxisDependency.RIGHT
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.0f".format(value)
                }
            }
        }

        val weightDataSet = LineDataSet(weightEntry, "體重").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.RED
            setDrawCircles(false)
            setDrawCircleHole(false)
            setDrawFilled(false)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.2f".format(value)
                }
            }
        }

        val fatRateDataSet = LineDataSet(fatRateEntry, "體脂").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.BLUE
            setDrawCircles(false)
            setDrawCircleHole(false)
            setDrawFilled(false)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.2f%%".format(value)
                }
            }
        }


        val caloriesDataForWeight = BarData(caloriesDataSetForWeight)
        val caloriesDataForFatRate = BarData(caloriesDataSetForWeight)
        val weightData = LineData(weightDataSet)
        val fatRateData = LineData(fatRateDataSet)


        val weightCaloriesData = CombinedData().apply {
            setData(caloriesDataForWeight)
            setData(weightData)
        }

        val fatRateCaloriesData = CombinedData().apply {
            setData(caloriesDataForFatRate)
            setData(fatRateData)
        }

        binding.chartWeightCalories.apply {
            data = weightCaloriesData

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return xLabelMap[value]?.yearWeek?.getFirstDate()
                        ?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) ?: ""
                }
            }

            xAxis.axisMaximum = caloriesDataForWeight.xMax + 0.5f

            setXAxisRenderer(
                FixedLabelXAxisRender(
                    viewPortHandler,
                    xAxis,
                    getTransformer(YAxis.AxisDependency.RIGHT),
                    xLabelPosition
                )
            )
            invalidate()
        }

        binding.chartFatRateCalories.apply {
            data = fatRateCaloriesData

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return xLabelMap[value]?.yearWeek?.getFirstDate()
                        ?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) ?: ""
                }
            }
            xAxis.axisMaximum = caloriesDataForFatRate.xMax + 0.5f

            setXAxisRenderer(
                FixedLabelXAxisRender(
                    viewPortHandler,
                    xAxis,
                    getTransformer(YAxis.AxisDependency.RIGHT),
                    xLabelPosition
                )
            )
            invalidate()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}