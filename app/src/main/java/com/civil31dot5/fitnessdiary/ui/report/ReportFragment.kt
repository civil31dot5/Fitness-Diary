package com.civil31dot5.fitnessdiary.ui.report

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.domain.model.WeekReport
import com.civil31dot5.fitnessdiary.ui.utility.FixedLabelXAxisRender
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReportScreen(viewModel)
            }
        }
    }

    @Composable
    fun ReportScreen(viewModel: ReportViewModel) {

        val chartData by viewModel.weekReport.collectAsStateWithLifecycle()

        ReportContent(
            chartData = chartData.sortedBy { it.yearWeek }
        )
    }

    @Composable
    fun ReportContent(
        chartData: List<WeekReport> = emptyList()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "體重與運動卡路里 週變化圖",
                modifier = Modifier.padding(start = 8.dp)
            )
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context -> CombinedChart(context).apply { initWeightCaloriesChart(this) } },
                update = { chart -> setWeightCaloriesChart(chart, chartData) }
            )

            Text(
                text = "體脂率與運動卡路里 週變化圖",
                modifier = Modifier.padding(start = 8.dp)
            )
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context -> CombinedChart(context).apply { initFatRateCaloriesChart(this) } },
                update = { chart -> setFatRateCaloriesChart(chart, chartData) }
            )

        }
    }

    private fun initWeightCaloriesChart(chart: CombinedChart) {
        chart.apply {
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

    }

    private fun initFatRateCaloriesChart(chart: CombinedChart) {
        chart.apply {
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

    private fun setWeightCaloriesChart(chart: CombinedChart, weekReposts: List<WeekReport>) {

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

        val caloriesDataForWeight = BarData(caloriesDataSetForWeight)
        val weightData = LineData(weightDataSet)

        val weightCaloriesData = CombinedData().apply {
            setData(caloriesDataForWeight)
            setData(weightData)
        }

        chart.apply {
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
    }

    private fun setFatRateCaloriesChart(chart: CombinedChart, weekReposts: List<WeekReport>) {

        val xLabelMap = mutableMapOf<Float, WeekReport>()

        weekReposts.forEachIndexed { index, weekReport ->
            xLabelMap.put(index.toFloat(), weekReport)
        }

        val xLabelPosition = List(weekReposts.size) { index -> index.toFloat() }.toFloatArray()

        val caloriesEntry = weekReposts.mapIndexed { index, weekReport ->
            BarEntry(index.toFloat(), weekReport.weekSportCalories.toFloat(), weekReport)
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

        val caloriesDataForFatRate = BarData(caloriesDataSetForWeight)
        val fatRateData = LineData(fatRateDataSet)

        val fatRateCaloriesData = CombinedData().apply {
            setData(caloriesDataForFatRate)
            setData(fatRateData)
        }

        chart.apply {
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


}