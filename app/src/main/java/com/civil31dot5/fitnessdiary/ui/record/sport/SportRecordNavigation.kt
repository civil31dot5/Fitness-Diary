package com.civil31dot5.fitnessdiary.ui.record.sport

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val sportRecordRoute = "sportRecord"

fun NavGraphBuilder.sportRecordScreen(){
    composable(
        route = sportRecordRoute
    ){
        SportRecordRoute()
    }
}