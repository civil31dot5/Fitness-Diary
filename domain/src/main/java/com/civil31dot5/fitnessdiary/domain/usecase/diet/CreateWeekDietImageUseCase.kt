package com.civil31dot5.fitnessdiary.domain.usecase.diet

import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class CreateWeekDietImageUseCase @Inject constructor(
    private val repository: RecordRepository
) {
    suspend operator fun invoke(from: LocalDate, to: LocalDate): File {
        return repository.createWeekDietImage(from, to)
    }
}