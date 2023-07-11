package com.civil31dot5.fitnessdiary.ui.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kizitonwose.calendar.core.CalendarDay
import java.time.LocalDate

const val homeRoute = "home"

fun NavGraphBuilder.homeScreen(
    onDayClick: (LocalDate) -> Unit = {},
    onAddDietClick: () -> Unit = {}
){
    composable(
        route = homeRoute
    ){
        HomeRoute(
            onDayClick = onDayClick,
            onAddDietClick = onAddDietClick
        )
    }
}