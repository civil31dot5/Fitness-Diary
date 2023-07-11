package com.civil31dot5.fitnessdiary.ui.record.bodyshape

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val bodyShapeScreenRoute = "bodyShapeScreen"

fun NavGraphBuilder.bodyShapeScreen(
    onAddRecordClick: () -> Unit
){
    composable(
        route = bodyShapeScreenRoute
    ){
        BodyShapeRecordRoute(
            onAddRecordClick = onAddRecordClick
        )
    }
}