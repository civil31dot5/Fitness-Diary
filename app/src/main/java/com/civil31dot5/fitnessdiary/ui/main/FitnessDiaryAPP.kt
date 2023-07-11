package com.civil31dot5.fitnessdiary.ui.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.ui.record.sport.SportRecordScreenAction


@Composable
fun FitnessDiaryAPP() {

    val appState = rememberFitnessDiaryAppState()

    ModalNavigationDrawer(
        drawerState = appState.drawerState,
        drawerContent = {
            FitnessDiaryDrawer(
                screens = appState.topLevelScreens,
                onNavToScreen = { appState.navToTopLevelScreen(it) },
                currentScreen = appState.currentScreen
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                FitnessDiaryTopBar(
                    currentScreen = appState.currentScreen,
                    onDrawerIconClick = { appState.openDrawer() },
                    onBackIconClick = { appState.navController.navigateUp() }
                )
            }
        ) { paddingValues ->
            FitnessDiaryNavHost(
                modifier = Modifier.padding(paddingValues),
                appState = appState
            )
        }
    }
}

@Composable
fun FitnessDiaryDrawer(
    screens: List<ScreenConfig>,
    onNavToScreen: (ScreenConfig) -> Unit,
    currentScreen: ScreenConfig?,
) {

    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        screens.forEach { config ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painterResource(id = getTopLevelScreenIconResId(config)),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = config.titleResId)) },
                selected = config == currentScreen,
                onClick = { onNavToScreen(config) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

fun getTopLevelScreenIconResId(screenConfig: ScreenConfig): Int {
    return when (screenConfig) {
        ScreenConfig.HOME -> R.drawable.ic_home
        ScreenConfig.DIET_RECORD -> R.drawable.ic_food_bank
        ScreenConfig.SPORT_RECORD -> R.drawable.ic_sports
        ScreenConfig.BODY_SHAPE -> R.drawable.ic_monitor_weight
        ScreenConfig.REPORT -> R.drawable.ic_analytics
        ScreenConfig.BACKUP_RESTORE -> R.drawable.ic_import_export
        else -> throw IllegalStateException("wrong screen")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessDiaryTopBar(
    currentScreen: ScreenConfig?,
    onDrawerIconClick: () -> Unit,
    onBackIconClick: () -> Unit
) {
    if (currentScreen == null || !currentScreen.showTopBar) return

    TopAppBar(
        navigationIcon = {
            if (currentScreen.isTopLevel) {
                IconButton(onClick = onDrawerIconClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                }
            } else {
                IconButton(onClick = onBackIconClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            }
        },
        title = { Text(text = stringResource(id = currentScreen.titleResId)) },
        actions = {
            if (currentScreen == ScreenConfig.SPORT_RECORD){
                SportRecordScreenAction()
            }
        }
    )
}
