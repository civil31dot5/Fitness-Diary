package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.AddBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.ui.base.AddPhotoRecordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddBodyShapeRecordViewModel @Inject constructor(
    private val addBodyShapeRecordUseCase: AddBodyShapeRecordUseCase
) : AddPhotoRecordViewModel() {

    init {
        setName("體態紀錄")
    }

    private var _weightFlow = MutableStateFlow("")
    val weightFlow = _weightFlow.asStateFlow()

    private var _fatRateFlow = MutableStateFlow("")
    val fatRateFlow = _fatRateFlow.asStateFlow()

    fun updateWeight(weight: String){
        _weightFlow.update { weight }
    }

    fun updateFatRate(fatRate: String){
        _fatRateFlow.update { fatRate }
    }

    fun submit() = viewModelScope.launch {

        setLoading(true)

        val dietRecord = BodyShapeRecord(
            UUID.randomUUID().toString(),
            basicRecordData.value.name,
            basicRecordData.value.dateTime,
            basicRecordData.value.note,
            photoRecordData.value.selectedPhotos.toPersistentList(),
            _weightFlow.value.toDouble(),
            _fatRateFlow.value.toDoubleOrNull()
        )

        setAddRecordResult(addBodyShapeRecordUseCase(dietRecord))
    }

}