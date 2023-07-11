package com.civil31dot5.fitnessdiary.ui.backuprestore

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val backupRestoreDataRoute = "backupRestoreData"

fun NavGraphBuilder.backupRestoreDataScreen(){
    composable(
        route = backupRestoreDataRoute
    ){
        BackupRestoreDataRoute()
    }
}