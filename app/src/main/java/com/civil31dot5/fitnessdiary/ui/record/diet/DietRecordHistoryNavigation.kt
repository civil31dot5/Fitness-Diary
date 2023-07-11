package com.civil31dot5.fitnessdiary.ui.record.diet

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val dietRecordHistoryRoute = "dietRecordHistory"

fun NavGraphBuilder.dietRecordHistoryScreen(){
    composable(
        route = dietRecordHistoryRoute
    ){
        DietRecordRoute()
    }
}