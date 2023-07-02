package com.civil31dot5.fitnessdiary.ui.record.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.extraFile
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DietRecordHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FitnessDiaryTheme {
                    DietRecordScreen()
                }
            }
        }
    }

}

@Composable
fun DietRecordScreen(
    viewModel: DietRecordHistoryViewModel = viewModel()
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
fun ConfirmDeleteDialog(
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "確認刪除?") },
        confirmButton = {
            TextButton(
                onClick = onDeleteClick
            ) {
                Text("確認")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        }
    )
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
    onEditClick: (DietRecord) -> Unit,
    onDeleteClick: (DietRecord) -> Unit,
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

        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp)
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePager(images: List<RecordImage>) {
    val pagerState = rememberPagerState()

    Box {
        HorizontalPager(
            pageCount = images.size,
            state = pagerState
        ) { index ->
            Box(
                modifier = Modifier.aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(images[index].extraFile(LocalContext.current))
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = images[index].note,
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        if (images.size > 1) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }

    }

}


