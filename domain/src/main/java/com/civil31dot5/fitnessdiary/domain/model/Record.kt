package com.civil31dot5.fitnessdiary.domain.model

import java.time.LocalDateTime

/**
 * Record 紀錄
 *
 * @property id ID
 * @property name 名稱
 * @property dateTime 時間
 * @property note 備註
 */
sealed class Record(
    open val id: String,
    open val name: String,
    open val dateTime: LocalDateTime,
    open val note: String
)

/**
 * Diet record 飲食紀錄
 *
 * @property id ID
 * @property name 名稱
 * @property dateTime 時間
 * @property note 備註
 * @property images 飲食照片
 */
data class DietRecord(
    override val id: String,
    override val name: String,
    override val dateTime: LocalDateTime,
    override val note: String,
    val images: List<RecordImage>
): Record(id, name, dateTime, note)

/**
 * Feeling record 心情紀錄
 *
 * @property id ID
 * @property name 名稱
 * @property dateTime 時間
 * @property note 備註
 * @property images 照片
 */
data class FeelingRecord(
    override val id: String,
    override val name: String,
    override val dateTime: LocalDateTime,
    override val note: String,
    val images: List<RecordImage>,
): Record(id, name, dateTime, note)

/**
 * Body shape record 體態紀錄
 *
 * @property id ID
 * @property name 名稱
 * @property dateTime 時間
 * @property note 備註
 * @property images 照片
 * @property weight 體重
 * @property bodyFatPercentage 體脂率
 */
data class BodyShapeRecord(
    override val id: String,
    override val name: String,
    override val dateTime: LocalDateTime,
    override val note: String,
    val images: List<RecordImage>,
    val weight: Double,
    val bodyFatPercentage: Double? = null,
): Record(id, name, dateTime, note)

/**
 * Drink water record 喝水紀錄
 *
 * @property id ID
 * @property name 名稱
 * @property dateTime 時間
 * @property note 備註
 * @property volume 喝水量ml
 */
data class DrinkWaterRecord(
    override val id: String,
    override val name: String,
    override val dateTime: LocalDateTime,
    override val note: String,
    val volume: Int
): Record(id, name, dateTime, note)


/**
 * Record image 紀錄的照片
 *
 * @property id ID
 * @property filePath 檔案路徑
 * @property note 備註
 */
data class RecordImage(
    val id: String,
    val filePath: String,
    val note: String = ""
)