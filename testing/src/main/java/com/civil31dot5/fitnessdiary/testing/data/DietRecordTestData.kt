package com.civil31dot5.fitnessdiary.testing.data

import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import com.civil31dot5.fitnessdiary.domain.model.RecordImage
import java.time.LocalDateTime

val dietRecordTestData = listOf(
    DietRecord(
        id = "dietRecordTestId1",
        name = "飲食紀錄",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            18,
            0
        ),
        note = "備註(DietRecord)",
        images = listOf(
            RecordImage(
                id = "dietRecordTestId1_ImageId1",
                filePath = "images/dietRecordTestId1_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        )
    ),
    DietRecord(
        id = "dietRecordTestId2",
        name = "飲食紀錄",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            16,
            0
        ),
        note = "備註(DietRecord)",
        images = listOf(
            RecordImage(
                id = "dietRecordTestId2_ImageId1",
                filePath = "images/dietRecordTestId2_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        )
    ),
    DietRecord(
        id = "dietRecordTestId3",
        name = "飲食紀錄",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            14,
            0
        ),
        note = "備註(DietRecord)",
        images = listOf(
            RecordImage(
                id = "dietRecordTestId3_ImageId1",
                filePath = "images/dietRecordTestId3_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        )
    ),
    DietRecord(
        id = "dietRecordTestId4",
        name = "飲食紀錄",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            12,
            0
        ),
        note = "備註(DietRecord)",
        images = listOf(
            RecordImage(
                id = "dietRecordTestId4_ImageId1",
                filePath = "images/dietRecordTestId4_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        )
    ),
    DietRecord(
        id = "dietRecordTestId5",
        name = "飲食紀錄",
        dateTime = LocalDateTime.of(
            2023,
            7,
            16,
            10,
            0
        ),
        note = "備註(DietRecord)",
        images = listOf(
            RecordImage(
                id = "dietRecordTestId5_ImageId1",
                filePath = "images/dietRecordTestId5_ImageId1.jpg",
                note = "備註(DietImage)"
            )
        )
    ),
)