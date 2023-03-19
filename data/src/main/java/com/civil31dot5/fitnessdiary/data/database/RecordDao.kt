package com.civil31dot5.fitnessdiary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface RecordDao {

    @Insert
    suspend fun insertDietRecord(dietRecord: DietRecordEntity)

    @Insert
    suspend fun insertRecordImage(image: RecordImageEntity)

    @Transaction
    @Query("SELECT * FROM diet_record")
    suspend fun getAllDietRecord(): List<DietRecordWithImages>

}