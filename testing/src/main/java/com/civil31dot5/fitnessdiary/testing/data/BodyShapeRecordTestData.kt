package com.civil31dot5.fitnessdiary.testing.data

import com.civil31dot5.fitnessdiary.domain.model.BodyShapeRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import java.time.LocalDateTime

val bodyShapeRecordTestData = listOf(
    BodyShapeRecord(
        id = "bodyShapeRecordTestId1",
        name = "體態紀錄(2023/6/18)",
        dateTime = LocalDateTime.of(
            2023,
            6,
            18,
            18,
            0
        ),
        note = "備註(BodyShapeRecord)",
        images = listOf(
            RecordImage(
                id = "bodyShapeRecordTestId1_ImageId1",
                filePath = "images/bodyShapeRecordTestId1_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        ),
        weight = 80.0,
        bodyFatPercentage = 20.0
    ),
    BodyShapeRecord(
        id = "bodyShapeRecordTestId2",
        name = "體態紀錄(2023/6/25)",
        dateTime = LocalDateTime.of(
            2023,
            6,
            25,
            18,
            0
        ),
        note = "備註(BodyShapeRecord)",
        images = listOf(
            RecordImage(
                id = "bodyShapeRecordTestId2_ImageId1",
                filePath = "images/bodyShapeRecordTestId2_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        ),
        weight = 79.0,
        bodyFatPercentage = 19.5
    ),
    BodyShapeRecord(
        id = "bodyShapeRecordTestId3",
        name = "體態紀錄(2023/7/2)",
        dateTime = LocalDateTime.of(
            2023,
            7,
            2,
            18,
            0
        ),
        note = "備註(BodyShapeRecord)",
        images = listOf(
            RecordImage(
                id = "bodyShapeRecordTestId3_ImageId1",
                filePath = "images/bodyShapeRecordTestId3_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        ),
        weight = 80.0,
        bodyFatPercentage = 20.5
    ),
    BodyShapeRecord(
        id = "bodyShapeRecordTestId4",
        name = "體態紀錄(2023/7/9)",
        dateTime = LocalDateTime.of(
            2023,
            7,
            9,
            18,
            0
        ),
        note = "備註(BodyShapeRecord)",
        images = listOf(
            RecordImage(
                id = "bodyShapeRecordTestId4_ImageId1",
                filePath = "images/bodyShapeRecordTestId4_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        ),
        weight = 78.0,
        bodyFatPercentage = 18.0
    ),
    BodyShapeRecord(
        id = "bodyShapeRecordTestId5",
        name = "體態紀錄(2023/7/16)",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            18,
            0
        ),
        note = "備註(BodyShapeRecord)",
        images = listOf(
            RecordImage(
                id = "bodyShapeRecordTestId5_ImageId1",
                filePath = "images/bodyShapeRecordTestId5_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        ),
        weight = 79.0,
        bodyFatPercentage = 19.0
    ),

)