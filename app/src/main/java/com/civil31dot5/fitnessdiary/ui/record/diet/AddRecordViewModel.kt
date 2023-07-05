package com.civil31dot5.fitnessdiary.ui.record.diet

import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.usecase.diet.AddDietRecordUseCase
import com.civil31dot5.fitnessdiary.ui.base.AddPhotoRecordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddDietRecordViewModel @Inject constructor(
    private val addDietRecordUseCase: AddDietRecordUseCase
) : AddPhotoRecordViewModel() {

    init {
        setName("飲食紀錄")
    }

    fun submit() = viewModelScope.launch {

        setLoading(true)

        val dietRecord = DietRecord(
            UUID.randomUUID().toString(),
            basicRecordData.value.name,
            basicRecordData.value.dateTime,
            basicRecordData.value.note,
            photoRecordData.value.selectedPhotos
        )

        setAddRecordResult(addDietRecordUseCase(dietRecord))
    }

}