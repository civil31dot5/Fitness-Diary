package com.civil31dot5.fitnessdiary.ui.main

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.civil31dot5.fitnessdiary.ui.backuprestore.backupRestoreDataRoute
import com.civil31dot5.fitnessdiary.ui.home.homeRoute
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.addBodyShapeRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.bodyShapeScreenRoute
import com.civil31dot5.fitnessdiary.ui.record.diet.addDietRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.diet.dietRecordHistoryRoute
import com.civil31dot5.fitnessdiary.ui.record.sport.sportRecordRoute
import com.civil31dot5.fitnessdiary.ui.report.dayRecordRoute
import com.civil31dot5.fitnessdiary.ui.report.reportRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun rememberFitnessDiaryAppState(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope  = rememberCoroutineScope(),
): FitnessDiaryAppState {
    return remember(
        navController,
        drawerState,
        coroutineScope
    ) {
        FitnessDiaryAppState(
            navController,
            drawerState,
            coroutineScope,
        )
    }
}

@Stable
class FitnessDiaryAppState(
    val navController: NavHostController,
    val drawerState: DrawerState,
    val scope: CoroutineScope,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentScreen: ScreenConfig?
        @Composable get() = currentDestination?.route?.let {  currentRoute ->
            ScreenConfig.values().firstOrNull { it.route == currentRoute }
        }

    val currentScreen2 : ScreenConfig?
        @Composable get() = when(currentDestination?.route){
            homeRoute -> ScreenConfig.HOME
            dietRecordHistoryRoute -> ScreenConfig.DIET_RECORD
            sportRecordRoute -> ScreenConfig.SPORT_RECORD
            bodyShapeScreenRoute -> ScreenConfig.BODY_SHAPE
            reportRoute -> ScreenConfig.REPORT
            backupRestoreDataRoute -> ScreenConfig.BACKUP_RESTORE
            addDietRecordRoute -> ScreenConfig.ADD_DIET_RECORD
            addBodyShapeRecordRoute -> ScreenConfig.ADD_BODY_SHAPE
            dayRecordRoute -> ScreenConfig.DAY_REPORT
            else -> null
        }

    val topLevelScreens = ScreenConfig.values().filter { it.isTopLevel }

    fun navToTopLevelScreen(screen: ScreenConfig) {
        closeDrawer()
        navController.navigate(screen.route){
            this.launchSingleTop = true
        }
    }

    fun openDrawer() {
        scope.launch { drawerState.open() }
    }

    fun closeDrawer(){
        scope.launch { drawerState.close() }
    }
}