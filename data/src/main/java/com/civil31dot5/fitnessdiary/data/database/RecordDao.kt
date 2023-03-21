package com.civil31dot5.fitnessdiary.data.database

import androidx.room.*
import com.civil31dot5.fitnessdiary.domain.model.DietRecord
import kotlinx.coroutines.flow.Flow


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

}