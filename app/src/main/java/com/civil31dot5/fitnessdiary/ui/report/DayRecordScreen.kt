package com.civil31dot5.fitnessdiary.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.BodyShapeRecordCard
import com.civil31dot5.fitnessdiary.ui.record.diet.DietRecordCard
import com.civil31dot5.fitnessdiary.ui.record.sport.SportRecordCard
import java.time.format.DateTimeFormatter


@Composable
fun DayRecordRoute(
    viewModel: DayRecordViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDateFlow.collectAsStateWithLifecycle(initialValue = null)
    val dayRecords by viewModel.selectedDateRecords.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Text(
                text = selectedDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        items(
            count = dayRecords.size,
            key = { index -> dayRecords[index].id },
            contentType = { index -> dayRecords[index].javaClass }
        ) { index ->
            val record = dayRecords[index]
            when (record) {
                is DietRecord -> {
                    DietRecordCard(record = record)
                }

                is StravaSportRecord -> {
                    SportRecordCard(
                        record.dateTime,
                        record.type,
                        record.calories,
                        record.elapsedTimeSec
                    )
                }

                is BodyShapeRecord -> {
                    BodyShapeRecordCard(record = record)
                }

                else -> {}
            }
        }
    }
}