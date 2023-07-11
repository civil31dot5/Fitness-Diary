package com.civil31dot5.fitnessdiary.ui.record.diet

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink

const val addDietRecordRoute = "addDietRecord"

fun NavController.navToAddDietRecord(navOptions: NavOptions? = null){
    navigate(addDietRecordRoute, navOptions)
}

fun NavGraphBuilder.addDietRecordScreen(
    onNavigateUp: () -> Unit = {}
){
    composable(
        route = addDietRecordRoute,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "fitnessdiary://add_diet_record"
            }
        )
    ){
        AddDietRecordRoute(
            onNavigateUp = onNavigateUp
        )
    }
}