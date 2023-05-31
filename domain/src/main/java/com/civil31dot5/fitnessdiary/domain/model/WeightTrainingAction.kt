package com.civil31dot5.fitnessdiary.domain.model


data class WeightTrainingAction(
    val name: String,
    val reps: Int,
    val load: Double,
    val loadUnit: LoadUnit
)

enum class LoadUnit {
    Kg, Lb
}