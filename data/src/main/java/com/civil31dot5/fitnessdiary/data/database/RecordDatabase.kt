package com.civil31dot5.fitnessdiary.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(value = [Converter::class])
@Database(
    entities = [
        DietRecordEntity::class,
        RecordImageEntity::class,
        StravaSportEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun getRecordDao(): RecordDao

    abstract fun getStravaSportDao(): StravaSportDao

}