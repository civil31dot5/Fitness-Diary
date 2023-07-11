package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.ui.utility.AddRecordButton
import com.civil31dot5.fitnessdiary.ui.utility.ConfirmDeleteDialog
import com.civil31dot5.fitnessdiary.ui.utility.ImagePager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BodyShapeRecordRoute(
    viewModel: BodyShapeRecordViewModel = hiltViewModel(),
    onAddRecordClick: () -> Unit = {}
) {

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    var toDeleteRecord: BodyShapeRecord? by remember { mutableStateOf(null) }

    BodyShapeRecordContent(
        chartData = uiState.chartData,
        bodyShapeRecord = uiState.bodyShapeRecord,
        onAddRecordClick = onAddRecordClick,
        onEditRecordClick = {},
        onDeleteRecordClick = {
            toDeleteRecord = it
        }
    )

    if (toDeleteRecord != null){
        ConfirmDeleteDialog(
            onDeleteClick = {
                viewModel.deleteRecord(toDeleteRecord!!)
                toDeleteRecord = null
            },
            onDismiss = {
                toDeleteRecord = null
            }
        )
    }

}


@Composable
fun BodyShapeRecordContent(
    chartData: List<BodyShapeRecordViewModel.BodyShapeChartData> = emptyList(),
    bodyShapeRecord: List<BodyShapeRecord> = emptyList(),
    onAddRecordClick: () -> Unit = {},
    onEditRecordClick: (BodyShapeRecord) -> Unit = {},
    onDeleteRecordClick: (BodyShapeRecord) -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                AndroidView(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    factory = { context -> LineChart(context).apply { initChart(this) } },
                    update = { lineChart -> setChartData(lineChart, chartData) }
                )
            }

            items(bodyShapeRecord, key = { it.id }){
                BodyShapeRecordCard(
                    record = it,
                    onEditRecordClick = onEditRecordClick,
                    onDeleteRecordClick = onDeleteRecordClick,
                )
            }
        }

        AddRecordButton(
            onClick = onAddRecordClick,
            contentDescription = "add bodyShape record",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

}

fun initChart(lineChart: LineChart) {
    lineChart.apply {

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

fun setChartData(lineChart: LineChart, chartData: List<BodyShapeRecordViewModel.BodyShapeChartData>) {
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

    lineChart.xAxis.apply {
        setLabelCount(dateTimeIndex.size, false)
        labelRotationAngle = 90f
        axisMinimum = -0.5f
        axisMaximum = weightDataSet.xMax + 0.5f
    }
    lineChart.xAxis.valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val dateTime = dateTimeIndex[value] ?: return ""
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        }
    }

    lineChart.data = lineData
    lineChart.invalidate()
}

@Composable
fun BodyShapeRecordCard(
    record: BodyShapeRecord,
    onEditRecordClick: ((BodyShapeRecord) -> Unit)? = null,
    onDeleteRecordClick: ((BodyShapeRecord) -> Unit)? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        ImagePager(record.images)
        Text(
            text = record.name, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Text(
            text = record.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )

        Text(
            text = "體重:%.1f".format(record.weight), style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )

        if (record.bodyFatPercentage != null){
            Text(
                text = "體脂率:%.1f%%".format(record.bodyFatPercentage), style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
        }

        if (onEditRecordClick != null && onDeleteRecordClick != null){
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            ) {
                Button(
                    enabled = false,
                    onClick = { onEditRecordClick(record) }
                ) {
                    Text(text = "Edit")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = { onDeleteRecordClick(record) }) {
                    Text(text = "Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}