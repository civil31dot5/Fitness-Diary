package com.civil31dot5.fitnessdiary.ui.report

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
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
import java.time.format.DateTimeFormatter

@Composable
fun ReportRoute(
    viewModel: ReportViewModel = hiltViewModel()
) {
    val chartData by viewModel.weekReport.collectAsStateWithLifecycle()

    ReportContent(
        chartData = chartData.sortedBy { it.yearWeek }
    )
}

@Composable
fun ReportContent(
    chartData: List<WeekReport> = emptyList()
) {
    val chartColor = MaterialTheme.colorScheme.onSurface

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
            factory = { context -> CombinedChart(context).apply { initWeightCaloriesChart(this, chartColor) } },
            update = { chart -> setWeightCaloriesChart(chart, chartData, chartColor) }
        )

        Text(
            text = "體脂率與運動卡路里 週變化圖",
            modifier = Modifier.padding(start = 8.dp)
        )
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context -> CombinedChart(context).apply { initFatRateCaloriesChart(this, chartColor) } },
            update = { chart -> setFatRateCaloriesChart(chart, chartData, chartColor) }
        )

    }
}

private fun initWeightCaloriesChart(
    chart: CombinedChart,
    chartColor: androidx.compose.ui.graphics.Color
) {
    chart.apply {
        description = null
        axisRight.apply {
            setDrawGridLines(false)
            axisMinimum = 0f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }

        axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 20f
            axisMaximum = 130f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }

        xAxis.apply {
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 90f
            axisMinimum = -0.5f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }
        setDrawBorders(true)
        extraBottomOffset = 60f
        isHighlightPerTapEnabled = false
        isHighlightPerDragEnabled = false
        setBorderColor(chartColor.toArgb())
        legend.textColor = chartColor.toArgb()
    }

}

private fun initFatRateCaloriesChart(
    chart: CombinedChart,
    chartColor: androidx.compose.ui.graphics.Color
) {
    chart.apply {
        description = null

        axisRight.apply {
            setDrawGridLines(false)
            axisMinimum = 0f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }

        axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 5f
            axisMaximum = 40f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }

        xAxis.apply {
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 90f
            axisMinimum = -0.5f
            axisLineColor = chartColor.toArgb()
            textColor = chartColor.toArgb()
        }
        setDrawBorders(true)
        extraBottomOffset = 60f
        isHighlightPerTapEnabled = false
        isHighlightPerDragEnabled = false
        setBorderColor(chartColor.toArgb())
        legend.textColor = chartColor.toArgb()
    }
}

private fun setWeightCaloriesChart(
    chart: CombinedChart,
    weekReposts: List<WeekReport>,
    chartColor: androidx.compose.ui.graphics.Color
) {

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
        valueTextColor = chartColor.toArgb()
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
        valueTextColor = chartColor.toArgb()
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

private fun setFatRateCaloriesChart(
    chart: CombinedChart,
    weekReposts: List<WeekReport>,
    chartColor: androidx.compose.ui.graphics.Color
) {

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
        valueTextColor = chartColor.toArgb()
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