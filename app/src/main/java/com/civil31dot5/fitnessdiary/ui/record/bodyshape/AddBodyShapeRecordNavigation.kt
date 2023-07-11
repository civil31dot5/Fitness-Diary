package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


const val addBodyShapeRecordRoute = "addBodyShapeRecord"

fun NavController.navToAddBodyShapeRecordRoute(navOptions: NavOptions? = null){
    navigate(addBodyShapeRecordRoute, navOptions)
}

fun NavGraphBuilder.addBodyShapeRecordScreen(
    onNavigateUp: () -> Unit = {}
){
    composable(
        route = addBodyShapeRecordRoute
    ){
        AddBodyShapeRecordRoute(
            onNavigateUp = onNavigateUp
        )
    }
}