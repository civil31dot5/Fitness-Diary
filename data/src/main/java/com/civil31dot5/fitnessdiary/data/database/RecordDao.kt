package com.civil31dot5.fitnessdiary.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


@Dao
interface RecordDao {

    @Insert
    suspend fun insertDietRecord(dietRecord: DietRecordEntity)

    @Insert
    suspend fun insertRecordImage(image: RecordImageEntity)

    @Transaction
    @Query("SELECT * FROM diet_record ORDER BY datetime ASC")
    fun getAllDietRecord(): Flow<List<DietRecordWithImages>>

    @Delete
    suspend fun deleteDietRecord(dietRecord: DietRecordEntity)

    @Delete
    suspend fun deleteRecordImage(image: RecordImageEntity)

    @Transaction
    @Query("SELECT * FROM diet_record WHERE datetime BETWEEN :from AND :to")
    suspend fun searchDietRecord(from: LocalDateTime, to: LocalDateTime): List<DietRecordWithImages>

    @Insert
    suspend fun insertBodyShapeRecord(bodyShapeRecord: BodyShapeRecordEntity)

    @Transaction
    @Query("SELECT * FROM body_shape_record ORDER BY datetime ASC")
    fun getAllBodyShapeRecord(): Flow<List<BodyShapeRecordWithImages>>

    @Delete
    suspend fun deleteBodyShapeRecord(bodyShapeRecord: BodyShapeRecordEntity)

}