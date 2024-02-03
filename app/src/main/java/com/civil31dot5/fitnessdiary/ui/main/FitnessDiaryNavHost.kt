package com.civil31dot5.fitnessdiary.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.civil31dot5.fitnessdiary.ui.backuprestore.backupRestoreDataScreen
import com.civil31dot5.fitnessdiary.ui.home.homeRoute
import com.civil31dot5.fitnessdiary.ui.home.homeScreen
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.addBodyShapeRecordScreen
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.bodyShapeScreen
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.navToAddBodyShapeRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.diet.addDietRecordScreen
import com.civil31dot5.fitnessdiary.ui.record.diet.dietRecordHistoryScreen
import com.civil31dot5.fitnessdiary.ui.record.diet.navToAddDietRecord
import com.civil31dot5.fitnessdiary.ui.record.sport.sportRecordScreen
import com.civil31dot5.fitnessdiary.ui.record.weekdiet.weekDietRoute
import com.civil31dot5.fitnessdiary.ui.report.ReportScreen
import com.civil31dot5.fitnessdiary.ui.report.dayRecordScreen
import com.civil31dot5.fitnessdiary.ui.report.navToDayRecordRoute

@Composable
fun FitnessDiaryNavHost(
    modifier: Modifier,
    appState: FitnessDiaryAppState
) {

    val navController = appState.navController

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = homeRoute,
    ) {

        homeScreen(
            onDayClick = { navController.navToDayRecordRoute(it) },
            onAddDietClick = { navController.navToAddDietRecord() }
        )

        bodyShapeScreen(
            onAddRecordClick = {
                navController.navToAddBodyShapeRecordRoute()
            }
        )

        addBodyShapeRecordScreen(
            onNavigateUp = { navController.navigateUp() }
        )

        dietRecordHistoryScreen()

        addDietRecordScreen(
            onNavigateUp = { navController.navigateUp() }
        )

        sportRecordScreen()

        ReportScreen()

        dayRecordScreen()

        backupRestoreDataScreen()

        weekDietRoute()
    }
}
