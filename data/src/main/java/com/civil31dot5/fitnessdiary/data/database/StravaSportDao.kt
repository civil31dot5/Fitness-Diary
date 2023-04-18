package com.civil31dot5.fitnessdiary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface StravaSportDao {

    @Insert
    suspend fun insertStravaSport(item: StravaSportEntity)

    @Query("SELECT * FROM strava_sport WHERE id = :id")
    suspend fun queryById(id: Long): StravaSportEntity?

    @Query("SELECT * FROM strava_sport WHERE datetime BETWEEN :from AND :to")
    fun query(from: LocalDateTime, to: LocalDateTime): Flow<List<StravaSportEntity>>

}