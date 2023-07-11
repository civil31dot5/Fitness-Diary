package com.civil31dot5.fitnessdiary.ui.report

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.time.LocalDate
import java.time.format.DateTimeFormatter


const val selectedDate = "selectedDate"
const val dayRecordRoute = "dayRecord/{$selectedDate}"

fun NavController.navToDayRecordRoute(date: LocalDate){
    val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    navigate(
        "dayRecord/$dateString"
    )
}

fun NavGraphBuilder.dayRecordScreen(){
    composable(
        route = dayRecordRoute,
        arguments = listOf(
            navArgument(selectedDate){ type = NavType.StringType }
        )
    ){
        DayRecordRoute()
    }
}