package com.civil31dot5.fitnessdiary.ui.record.weekdiet

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val weekDietRoute = "WeekDietRoute"

fun NavGraphBuilder.weekDietRoute(){
    composable(
        route = weekDietRoute
    ){
        WeekDietRoute()
    }
}