package com.civil31dot5.fitnessdiary.ui.record.diet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.ui.utility.ConfirmDeleteDialog
import com.civil31dot5.fitnessdiary.ui.utility.ImagePager
import java.time.format.DateTimeFormatter


@Composable
fun DietRecordRoute(
    viewModel: DietRecordHistoryViewModel = hiltViewModel()
) {
    val dietRecords by viewModel.dietRecordList.collectAsStateWithLifecycle()
    var toDeleteRecord: DietRecord? by remember { mutableStateOf(null) }

    DietRecordContent(
        dietRecords = dietRecords,
        onEditClick = {},
        onDeleteClick = {
            toDeleteRecord = it
        }
    )

    if (toDeleteRecord != null) {
        ConfirmDeleteDialog(
            onDeleteClick = {
                viewModel.deleteDietRecord(toDeleteRecord!!)
                toDeleteRecord = null
            },
            onDismiss = {
                toDeleteRecord = null
            }
        )
    }
}


@Composable
fun DietRecordContent(
    dietRecords: List<DietRecord>,
    onEditClick: (DietRecord) -> Unit,
    onDeleteClick: (DietRecord) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dietRecords, key = { it.id }) {
            DietRecordCard(
                record = it,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}


@Composable
fun DietRecordCard(
    record: DietRecord,
    onEditClick: ((DietRecord) -> Unit)? = null,
    onDeleteClick: ((DietRecord) -> Unit)? = null,
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
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )

        if (onEditClick != null && onDeleteClick != null){
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            ) {
                Button(
                    enabled = false,
                    onClick = { onEditClick(record) }
                ) {
                    Text(text = "Edit")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = { onDeleteClick(record) }) {
                    Text(text = "Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

