package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.ui.base.BaseAddPhotoContent
import com.civil31dot5.fitnessdiary.ui.base.BaseAddPhotoContentState
import com.civil31dot5.fitnessdiary.ui.base.InputFieldDate
import com.civil31dot5.fitnessdiary.ui.base.InputFieldName
import com.civil31dot5.fitnessdiary.ui.base.InputFieldNote
import com.civil31dot5.fitnessdiary.ui.base.InputFieldTime
import com.civil31dot5.fitnessdiary.ui.base.SelectedImageItem
import java.time.format.DateTimeFormatter


@Composable
fun AddBodyShapeRecordRoute(
    viewModel: AddBodyShapeRecordViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val state = remember { BaseAddPhotoContentState() }
    val recordData by viewModel.basicRecordData.collectAsStateWithLifecycle()
    val selectedPhotos by viewModel.photoRecordData.collectAsStateWithLifecycle()

    val weight by viewModel.weightFlow.collectAsStateWithLifecycle()
    val fatRate by viewModel.fatRateFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current

    if (recordData.addRecordSuccess == true) {
        Toast.makeText(LocalContext.current, "新增成功", Toast.LENGTH_LONG).show()
        onNavigateUp()
    }

    BaseAddPhotoContent(
        state = state,
        onAddPhoto = { viewModel.addPhoto(it) },
        onDateSelected = { viewModel.setDate(it) },
        onTimeSelected = { viewModel.setTime(it) }
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = "新增體態紀錄",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                InputFieldName(
                    modifier = Modifier.fillMaxWidth(),
                    name = recordData.name,
                    onNameUpdate = { viewModel.setName(it) }
                )
            }
            item {
                InputFieldDate(
                    modifier = Modifier.fillMaxWidth(),
                    dateString = recordData.date.toString(),
                    onClick = { state.showDatePicker() }
                )
            }
            item {
                InputFieldTime(
                    modifier = Modifier.fillMaxWidth(),
                    timeString = {
                        recordData.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                    },
                    onClick = { state.showTimePicker() }
                )
            }

            item {
                InputFieldWeight(
                    modifier = Modifier.fillMaxWidth(),
                    weight = { weight },
                    onWeightUpdate = { viewModel.updateWeight(it) }
                )
            }

            item {
                InputFieldFatRate(
                    modifier = Modifier.fillMaxWidth(),
                    fatRate = { fatRate },
                    onFatRateUpdate = { viewModel.updateFatRate(it) }
                )
            }

            item {
                InputFieldNote(
                    modifier = Modifier.fillMaxWidth(),
                    note = recordData.note,
                    onNoteUpdate = { viewModel.setNote(it) }
                )
            }

            items(selectedPhotos.selectedPhotos.size) { index ->
                val photo = selectedPhotos.selectedPhotos[index]
                SelectedImageItem(
                    photo = photo,
                    onImageNoteChanged = { image, note ->
                        viewModel.onRecordImageNoteChanged(image, note)
                    },
                    onDeleteClicked = { viewModel.deleteRecordImage(it) }
                )
            }

            if (selectedPhotos.isAddButtonVisible) {
                item {
                    TextButton(onClick = { state.showSelectPhotoFromDialog() }) {
                        Text(text = "新增照片")
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onNavigateUp() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "取消")
                    }

                    Button(
                        onClick = {
                            if (TextUtils.isEmpty(weight)) {
                                Toast.makeText(context, "請輸入體重", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                            viewModel.submit()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "確認")
                    }
                }
            }

        }
    }
}


@Composable
fun InputFieldWeight(
    modifier: Modifier = Modifier,
    weight: () -> String = { "" },
    onWeightUpdate: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = weight(),
        onValueChange = { s ->
            s.toDoubleOrNull()?.let {
                onWeightUpdate(s.trim())
            }
        },
        label = { Text(text = "體重") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_monitor_weight),
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
        maxLines = 1
    )
}

@Composable
fun InputFieldFatRate(
    modifier: Modifier = Modifier,
    fatRate: () -> String = { "" },
    onFatRateUpdate: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = fatRate(),
        onValueChange = { s ->
            s.toDoubleOrNull()?.let {
                onFatRateUpdate(s.trim())
            }
        },
        label = { Text(text = "體脂率") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_monitor_weight),
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
        maxLines = 1
    )
}