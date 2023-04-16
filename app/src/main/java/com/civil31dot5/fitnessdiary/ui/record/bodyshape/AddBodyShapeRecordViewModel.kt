package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.AddBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.ui.base.AddPhotoRecordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddBodyShapeRecordViewModel @Inject constructor(
    private val addBodyShapeRecordUseCase: AddBodyShapeRecordUseCase
): AddPhotoRecordViewModel() {

    init {
        setName("體態紀錄")
    }

    fun submit(note: String?, weight: Double, fatRate: Double?) = viewModelScope.launch {

        setLoading(true)

        val dietRecord = BodyShapeRecord(
            UUID.randomUUID().toString(),
            basicRecordData.value.name,
            basicRecordData.value.dateTime,
            note ?: "",
            photoRecordData.value.selectedPhotos,
            weight,
            fatRate
        )

        setAddRecordResult(addBodyShapeRecordUseCase(dietRecord))
    }

}