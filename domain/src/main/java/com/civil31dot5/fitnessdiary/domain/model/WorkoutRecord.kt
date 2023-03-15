package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

sealed class WorkoutRecord(
    open val datetime: LocalDateTime,
    open val name: String,
    open val calories: Int,
    open val elapsedSeconds: Int
) {
    data class AerobicExerciseRecord(
        override val datetime: LocalDateTime,
        override val name: String,
        override val calories: Int,
        override val elapsedSeconds: Int,
    ) : WorkoutRecord(datetime, name, calories, elapsedSeconds)

    data class WeightTrainingRecord(
        override val datetime: LocalDateTime,
        override val name: String,
        override val elapsedSeconds: Int,
    ) : WorkoutRecord(datetime, name, 0, elapsedSeconds)
}
