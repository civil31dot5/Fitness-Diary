package com.civil31dot5.fitnessdiary.testing.data

import com.civil31dot5.fitnessdiary.domain.model.StravaSportRecord
import java.time.LocalDateTime

val stravaSportRecordTestData = listOf(
    StravaSportRecord(
        stravaId = 1L,
        dateTime = LocalDateTime.of(
            2023,
            7,
            12,
            18,
            0
        ),
        name = "2023/7/12 RUNNING",
        distance = 1000.0,
        calories = 500.0,
        type = "RUNNING",
        elapsedTimeSec = 3600
    ),
    StravaSportRecord(
        stravaId = 2L,
        dateTime = LocalDateTime.of(
            2023,
            7,
            13,
            18,
            0
        ),
        name = "2023/7/13 RUNNING",
        distance = 1000.0,
        calories = 500.0,
        type = "RUNNING",
        elapsedTimeSec = 3600
    ),
    StravaSportRecord(
        stravaId = 3L,
        dateTime = LocalDateTime.of(
            2023,
            7,
            14,
            18,
            0
        ),
        name = "2023/7/14 RUNNING",
        distance = 1000.0,
        calories = 500.0,
        type = "RUNNING",
        elapsedTimeSec = 3600
    ),
    StravaSportRecord(
        stravaId = 4L,
        dateTime = LocalDateTime.of(
            2023,
            7,
            15,
            18,
            0
        ),
        name = "2023/7/15 RUNNING",
        distance = 1000.0,
        calories = 500.0,
        type = "RUNNING",
        elapsedTimeSec = 3600
    ),
    StravaSportRecord(
        stravaId = 5L,
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            18,
            0
        ),
        name = "2023/7/16 RUNNING",
        distance = 1000.0,
        calories = 500.0,
        type = "RUNNING",
        elapsedTimeSec = 3600
    ),
)