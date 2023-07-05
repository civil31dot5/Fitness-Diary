package com.civil31dot5.fitnessdiary.ui.record.diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import com.civil31dot5.fitnessdiary.extraFile
import com.civil31dot5.fitnessdiary.ui.base.BaseAddPhotoContent
import com.civil31dot5.fitnessdiary.ui.base.BaseAddPhotoContentState
import com.civil31dot5.fitnessdiary.ui.theme.FitnessDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class AddDietRecordFragment : Fragment() {

    private val viewModel: AddDietRecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireActivity()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FitnessDiaryTheme {
                    AddDietRecordScreen(viewModel)
                }
            }
        }
    }


    @Composable
    fun AddDietRecordScreen(viewModel: AddDietRecordViewModel) {

        val state = remember { BaseAddPhotoContentState() }
        val recordData by viewModel.basicRecordData.collectAsStateWithLifecycle()
        val selectedPhotos by viewModel.photoRecordData.collectAsStateWithLifecycle()

        if (recordData.addRecordSuccess == true) {
            Toast.makeText(LocalContext.current, "新增成功", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
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
                        text = "新增飲食紀錄",
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
                        dateString =  recordData.date.toString(),
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
                    InputFieldNote(
                        modifier = Modifier.fillMaxWidth(),
                        note = recordData.note,
                        onNoteUpdate = { viewModel.setNote(it) }
                    )
                }


                items(selectedPhotos.selectedPhotos.size) { index ->
                    val photo = selectedPhotos.selectedPhotos[index]
                    SelectedImageItem(photo)
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
                            onClick = { findNavController().navigateUp() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "取消")
                        }

                        Button(
                            onClick = { viewModel.submit() },
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
    fun InputFieldName(
        modifier: Modifier = Modifier,
        name: String = "name",
        onNameUpdate: (String) -> Unit = {}
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameUpdate,
            label = { Text(text = "名稱") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_food_bank),
                    contentDescription = null
                )
            },
            modifier = modifier
        )
    }


    @Composable
    fun InputFieldDate(
        modifier: Modifier = Modifier,
        dateString: String = "",
        onClick: () -> Unit = {}
    ) {
        OutlinedTextField(
            value = dateString,
            onValueChange = {},
            enabled = false,
            label = { Text(text = "日期") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit_calendar),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .clickable { onClick() }
                .then(modifier)
        )
    }

    @Composable
    fun InputFieldTime(
        modifier: Modifier = Modifier,
        timeString: () -> String = {""},
        onClick: () -> Unit = {}
    ) {
        OutlinedTextField(
            value = timeString(),
            onValueChange = {},
            enabled = false,
            label = { Text(text = "時間") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .clickable { onClick() }
                .then(modifier)
        )
    }

    @Composable
    fun InputFieldNote(
        modifier: Modifier = Modifier,
        note: String = "",
        onNoteUpdate: (String) -> Unit = {}
    ) {
        OutlinedTextField(
            value = note,
            onValueChange = onNoteUpdate,
            label = { Text(text = "備註") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_note),
                    contentDescription = null
                )
            },
            modifier = modifier
        )
    }

    @Composable
    fun SelectedImageItem(photo: RecordImage) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.extraFile(LocalContext.current))
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = photo.note,
                onValueChange = { note ->
                    viewModel.onRecordImageNoteChanged(photo, note)
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { viewModel.deleteRecordImage(photo) }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
            }

        }
    }

}