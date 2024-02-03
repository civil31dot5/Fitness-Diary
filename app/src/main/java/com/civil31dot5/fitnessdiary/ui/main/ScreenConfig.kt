package com.civil31dot5.fitnessdiary.ui.main

import com.civil31dot5.fitnessdiary.R
import com.civil31dot5.fitnessdiary.ui.backuprestore.backupRestoreDataRoute
import com.civil31dot5.fitnessdiary.ui.home.homeRoute
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.addBodyShapeRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.bodyshape.bodyShapeScreenRoute
import com.civil31dot5.fitnessdiary.ui.record.diet.addDietRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.diet.dietRecordHistoryRoute
import com.civil31dot5.fitnessdiary.ui.record.sport.sportRecordRoute
import com.civil31dot5.fitnessdiary.ui.record.weekdiet.weekDietRoute
import com.civil31dot5.fitnessdiary.ui.report.dayRecordRoute
import com.civil31dot5.fitnessdiary.ui.report.reportRoute

enum class ScreenConfig(
    val route: String,
    val titleResId: Int,
    val isTopLevel: Boolean,
    val showTopBar: Boolean
){

    HOME(
        route = homeRoute,
        titleResId = R.string.home,
        isTopLevel = true,
        showTopBar = true
    ),
    DIET_RECORD(
        route = dietRecordHistoryRoute,
        titleResId = R.string.diet_record,
        isTopLevel = true,
        showTopBar = true
    ),
    WEEK_DIET(
        route = weekDietRoute,
        titleResId = R.string.week_diet,
        isTopLevel = true,
        showTopBar = true
    ),
    SPORT_RECORD(
        route = sportRecordRoute,
        titleResId = R.string.sport_record,
        isTopLevel = true,
        showTopBar = true
    ),
    BODY_SHAPE(
        route = bodyShapeScreenRoute,
        titleResId = R.string.body_shape_record,
        isTopLevel = true,
        showTopBar = true
    ),
    REPORT(
        route = reportRoute,
        titleResId = R.string.report,
        isTopLevel = true,
        showTopBar = true
    ),
    BACKUP_RESTORE(
        route = backupRestoreDataRoute,
        titleResId = R.string.backup_restore,
        isTopLevel = true,
        showTopBar = true
    ),
    ADD_DIET_RECORD(
        route = addDietRecordRoute,
        titleResId = R.string.add_diet_record,
        isTopLevel = false,
        showTopBar = true
    ),
    ADD_BODY_SHAPE(
        route = addBodyShapeRecordRoute,
        titleResId = R.string.add_body_shape_record,
        isTopLevel = false,
        showTopBar = true
    ),
    DAY_REPORT(
        route = dayRecordRoute,
        titleResId = R.string.day_report,
        isTopLevel = false,
        showTopBar = true
    ),

}