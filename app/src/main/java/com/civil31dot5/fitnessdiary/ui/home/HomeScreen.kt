package com.civil31dot5.fitnessdiary.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import com.civil31dot5.fitnessdiary.ui.utility.AddRecordButton
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
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onDayClick: (LocalDate) -> Unit = {},
    onAddDietClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )

    LaunchedEffect(calendarState) {
        snapshotFlow { calendarState.firstVisibleMonth.yearMonth }
            .distinctUntilChanged()
            .collect {
                viewModel.selectYearMonth(it)
            }
    }

    HomeContent(
        calendarState,
        uiState.recordStatus,
        onDayClick,
        onAddDietClick
    )
}

@Composable
fun HomeContent(
    calendarState: CalendarState = rememberCalendarState(),
    recordStatus: Map<LocalDate, HomeViewModel.RecordStatus> = emptyMap(),
    onDayClick: (LocalDate) -> Unit = {},
    onAddDietClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = calendarState.firstVisibleMonth.yearMonth.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            RecordCalendar(calendarState, recordStatus, onDayClick)
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
    calendarState: CalendarState = rememberCalendarState(),
    recordStatus: Map<LocalDate, HomeViewModel.RecordStatus> = emptyMap(),
    onDayClick: (LocalDate) -> Unit = {}
) {
    val dayOfWeek = remember { daysOfWeek() }

    HorizontalCalendar(
        state = calendarState,
        dayContent = {
            val record = recordStatus[it.date]
            Day(
                day = it,
                onDayClick = onDayClick,
                hasDietRecord = record?.hasDietRecord == true,
                hasSportHistory = record?.hasSportHistory == true
            )
        },
        monthHeader = { DaysOfWeekTitle(daysOfWeek = dayOfWeek) }
    )
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
                    .padding(bottom = 4.dp)
            ) {
                if (hasDietRecord) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_fastfood),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.size(15.dp)
                    )
                }
                if (hasSportHistory) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_sports),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}
