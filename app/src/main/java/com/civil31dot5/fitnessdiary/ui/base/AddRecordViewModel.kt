package com.civil31dot5.fitnessdiary.ui.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
open class AddRecordViewModel @Inject constructor(

): ViewModel() {

    data class BasicRecordData(
        val name: String = "",
        val date: LocalDate = LocalDate.now(),
        val time: LocalTime = LocalTime.now(),
        val note: String = "",
        val isLoading: Boolean = false,
        val addRecordSuccess: Boolean? = null
    ){
        val dateTime: LocalDateTime
            get() = LocalDateTime.of(date, time)
    }

    private val _basicRecordDataFlow = MutableStateFlow(BasicRecordData())
    val basicRecordData = _basicRecordDataFlow.asStateFlow()

    fun setName(name: String){
        _basicRecordDataFlow.update { it.copy(name = name) }
    }

    fun setDate(newDate: LocalDate) {
        _basicRecordDataFlow.update { it.copy(date = newDate) }
    }

    fun setTime(newTime: LocalTime) {
        _basicRecordDataFlow.update { it.copy(time = newTime) }
    }

    fun setNote(note: String){
        _basicRecordDataFlow.update { it.copy(note = note) }
    }

    fun setLoading(isLoading:Boolean){
        _basicRecordDataFlow.update { it.copy(isLoading = isLoading) }
    }

    fun setAddRecordResult(success: Boolean){
        _basicRecordDataFlow.update { it.copy(addRecordSuccess = success, isLoading = false) }
    }

}