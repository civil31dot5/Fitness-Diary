package com.civil31dot5.fitnessdiary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(value = [Converter::class])
@Database(
    entities = [
        DietRecordEntity::class,
        RecordImageEntity::class],
    version = 1
)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun getRecordDao(): RecordDao

}