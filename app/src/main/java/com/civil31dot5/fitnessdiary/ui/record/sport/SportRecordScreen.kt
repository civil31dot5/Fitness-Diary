package com.civil31dot5.fitnessdiary.ui.record.sport

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun SportRecordRoute(
    viewModel: SportRecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SportRecordContent(uiState.sportHistory)
}

@Composable
fun SportRecordContent(sportHistory: List<StravaSportRecord>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(all = 8.dp)
    ) {
        items(sportHistory, key = { it.id }) { record ->
            SportRecordCard(
                record.dateTime,
                record.type,
                record.calories,
                record.elapsedTimeSec
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSportRecordContent() {
    FitnessDiaryTheme {
        SportRecordContent(
            listOf(
                StravaSportRecord(
                    0,
                    LocalDateTime.now(),
                    "running name",
                    13.0,
                    800.0,
                    "running",
                    3600
                ),
                StravaSportRecord(
                    1,
                    LocalDateTime.now(),
                    "evening bike",
                    45.0,
                    800.0,
                    "bike",
                    7200
                ),
            )
        )
    }
}

@Composable
fun SportRecordCard(dateTime: LocalDateTime, type: String, calories: Double, elapsedTimeSec: Long) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                Text(text = type.uppercase(), style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "%.0f卡路里".format(calories),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                text = LocalTime.MIN.plusSeconds(elapsedTimeSec)
                    .format(DateTimeFormatter.ISO_LOCAL_TIME)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSportRecordCard() {
    FitnessDiaryTheme {
        SportRecordCard(
            LocalDateTime.now(),
            "running",
            800.0,
            3600
        )
    }
}