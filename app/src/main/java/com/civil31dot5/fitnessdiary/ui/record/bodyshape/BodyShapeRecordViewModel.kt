package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.AddBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.DeleteBodyShapeRecordUseCase
import com.civil31dot5.fitnessdiary.domain.usecase.bodyshape.GetAllBodyShapeRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BodyShapeRecordViewModel @Inject constructor(
    private val getAllBodyShapeRecordUseCase: GetAllBodyShapeRecordUseCase,
    private val deleteBodyShapeRecordUseCase: DeleteBodyShapeRecordUseCase
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val chartData: List<BodyShapeChartData> = emptyList(),
        val bodyShapeRecord: List<BodyShapeRecord> = emptyList()
    )

    data class BodyShapeChartData(
        val dateTime: LocalDateTime, val weight: Double, val fatRate: Double? = null
    )

    private val _uiStateFlow = MutableStateFlow(UiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            getAllBodyShapeRecordUseCase.invoke().collect { record ->
                val chartData = record.sortedBy { it.dateTime }.take(10).map {
                    BodyShapeChartData(it.dateTime, it.weight, it.bodyFatPercentage)
                }

                val recordData = record.sortedByDescending { it.dateTime }

                _uiStateFlow.update { it.copy(chartData = chartData, bodyShapeRecord = recordData) }
            }
        }
    }

    fun deleteRecord(data: BodyShapeRecord) = viewModelScope.launch {
        deleteBodyShapeRecordUseCase.invoke(data)
    }

}