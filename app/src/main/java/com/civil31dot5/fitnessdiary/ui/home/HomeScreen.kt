package com.civil31dot5.fitnessdiary.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.domain.model.WeekReport
import com.civil31dot5.fitnessdiary.domain.usecase.RecordStatus
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import com.civil31dot5.fitnessdiary.ui.utility.AddRecordButton
import com.civil31dot5.fitnessdiary.ui.utility.FixedLabelXAxisRender
import com.github.mikephil.charting.charts.LineChart
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
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onDayClick: (LocalDate) -> Unit = {},
    onAddDietClick: () -> Unit = {}
) {
    val recordStatus by viewModel.recordStatus.collectAsStateWithLifecycle()
    val fatMassHistory by viewModel.fatMassHistory.collectAsStateWithLifecycle()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )

    LaunchedEffect(Unit) {
        snapshotFlow { calendarState.firstVisibleMonth.yearMonth }
            .distinctUntilChanged()
            .collect {
                viewModel.selectYearMonth(it)
            }
    }

    HomeContent(
        calendarState,
        recordStatus,
        fatMassHistory,
        onDayClick,
        onAddDietClick
    )
}

@Composable
fun HomeContent(
    calendarState: CalendarState = rememberCalendarState(),
    recordStatus: Map<LocalDate, RecordStatus> = emptyMap(),
    fatMassHistory: List<HomeViewModel.FatMassHistory> = emptyList(),
    onDayClick: (LocalDate) -> Unit = {},
    onAddDietClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = calendarState.firstVisibleMonth.yearMonth.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                RecordCalendar(
                    modifier = Modifier.padding(8.dp),
                    calendarState = calendarState,
                    recordStatus = recordStatus,
                    onDayClick = onDayClick
                )
            }
            item {
                FatMassChart(
                    modifier = Modifier.padding(8.dp),
                    fatMassHistory = fatMassHistory
                )
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }

        }

        AddRecordButton(
            onClick = onAddDietClick,
            contentDescription = "add diet record",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewHomeContent() {
    FitnessDiaryTheme {
        HomeContent()
    }
}

@Composable
fun RecordCalendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState = rememberCalendarState(),
    recordStatus: Map<LocalDate, RecordStatus> = emptyMap(),
    onDayClick: (LocalDate) -> Unit = {}
) {
    val dayOfWeek = remember { daysOfWeek() }

    Card(
        modifier = modifier
    ) {
        HorizontalCalendar(
            modifier = Modifier.padding(8.dp),
            state = calendarState,
            dayContent = {
                val record = recordStatus[it.date]
                Day(
                    day = it,
                    onDayClick = onDayClick,
                    hasDietRecord = record?.hasDietRecord == true,
                    hasSportHistory = record?.hasSportHistory == true,
                    hasBodyShapeRecord = record?.hasBodyShapeRecord == true
                )
            },
            monthHeader = { DaysOfWeekTitle(daysOfWeek = dayOfWeek) }
        )
    }

}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
fun PreviewRecordCalendar() {
    FitnessDiaryTheme {
        RecordCalendar()
    }
}

@Composable
fun Day(
    day: CalendarDay = CalendarDay(LocalDate.now(), DayPosition.MonthDate),
    hasDietRecord: Boolean = false,
    hasSportHistory: Boolean = false,
    hasBodyShapeRecord: Boolean = false,
    onDayClick: (LocalDate) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)// This is important for square sizing!
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onDayClick(day.date) }
            )
            .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (day.position == DayPosition.MonthDate) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(15.dp)
                    .padding(bottom = 4.dp),
            ) {
                if (hasDietRecord) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_fastfood),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (hasSportHistory) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_sports),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (hasBodyShapeRecord) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_monitor_weight),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DayPreview() {
    FitnessDiaryTheme {
        Day(
            hasDietRecord = true,
            hasBodyShapeRecord = true,
            hasSportHistory = true
        )
    }
}

@Composable
fun FatMassChart(
    modifier: Modifier = Modifier,
    fatMassHistory: List<HomeViewModel.FatMassHistory> = emptyList()
) {
    val chartColor = MaterialTheme.colorScheme.onSurface
    Card(
        modifier = modifier
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                LineChart(context).apply { initChart(this, chartColor) }
            },
            update = { chart -> updateFatMassChart(chart, fatMassHistory, chartColor) }
        )
    }
}
private fun initChart(
    chart: LineChart,
    chartColor: Color) {
    chart.apply {
        description = null
        axisRight.apply {
            isEnabled = false
        }

        axisLeft.apply {
            setDrawGridLines(false)
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

private fun updateFatMassChart(
    chart: LineChart,
    fatMassHistory: List<HomeViewModel.FatMassHistory>,
    chartColor: Color
) {

    val xLabelMap = mutableMapOf<Float, HomeViewModel.FatMassHistory>()

    fatMassHistory.forEachIndexed { index, fatMass ->
        xLabelMap.put(index.toFloat(), fatMass)
    }

    val xLabelPosition = List(fatMassHistory.size) { index -> index.toFloat() }.toFloatArray()


    val fatRateEntry = fatMassHistory.mapIndexed { index, fatMass ->
        Entry(index.toFloat(), fatMass.fatMass.toFloat(), fatMass)
    }

    val fatMassDataSet = LineDataSet(fatRateEntry, "體脂重量(kg)").apply {
        axisDependency = YAxis.AxisDependency.LEFT
        color = android.graphics.Color.RED
        setDrawCircles(false)
        setDrawCircleHole(false)
        setDrawFilled(false)
        valueTextSize = 14f
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "%.2f".format(value)
            }
        }
        valueTextColor = chartColor.toArgb()
    }

    val fatRateData = LineData(fatMassDataSet)

    chart.apply {
        data = fatRateData

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return xLabelMap[value]?.yearWeek?.getFirstDate()
                    ?.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) ?: ""
            }
        }
        xAxis.axisMaximum = fatRateData.xMax + 0.5f

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
