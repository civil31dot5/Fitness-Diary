package com.civil31dot5.fitnessdiary.ui.report

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val reportRoute = "Report"

fun NavGraphBuilder.ReportScreen(){
    composable(
        route = reportRoute
    ){
        ReportRoute()
    }
}