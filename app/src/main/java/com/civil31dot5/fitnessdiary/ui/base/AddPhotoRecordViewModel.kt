package com.civil31dot5.fitnessdiary.ui.base

import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
open class AddPhotoRecordViewModel @Inject constructor(

): AddRecordViewModel(){

    private var photoSizeLimit = 5

    data class PhotoRecordData(
        val selectedPhotos: List<RecordImage> = emptyList(),
        val isAddButtonVisible: Boolean = true
    )

    private val _photoRecordData = MutableStateFlow<PhotoRecordData>(PhotoRecordData())
    val photoRecordData = _photoRecordData.asStateFlow()

    fun addPhoto(filePath: String) {
        val recordImage = RecordImage(filePath = filePath)
        val newList = _photoRecordData.value.selectedPhotos.toMutableList().apply {
            add(recordImage)
        }
        val isAddButtonVisible = newList.size < photoSizeLimit
        _photoRecordData.update { it.copy(selectedPhotos = newList, isAddButtonVisible = isAddButtonVisible) }
    }

    fun deleteRecordImage(image: RecordImage) {
        val newList = _photoRecordData.value.selectedPhotos.toMutableList()
        newList.removeIf { it.id == image.id }
        val isAddButtonVisible = newList.size < photoSizeLimit
        _photoRecordData.update { it.copy(selectedPhotos = newList, isAddButtonVisible = isAddButtonVisible) }
    }

    fun onRecordImageNoteChanged(image: RecordImage, note: String) {
        val newList = _photoRecordData.value.selectedPhotos.toMutableList()
        newList.replaceAll {
            if (it.id == image.id){
                return@replaceAll it.copy(note = note)
            }
            return@replaceAll it
        }
        _photoRecordData.update { it.copy(selectedPhotos = newList) }
    }

}