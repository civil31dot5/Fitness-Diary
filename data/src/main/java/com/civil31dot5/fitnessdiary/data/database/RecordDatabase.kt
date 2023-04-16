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
        StravaSportEntity::class,
        BodyShapeRecordEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ]
)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun getRecordDao(): RecordDao

    abstract fun getStravaSportDao(): StravaSportDao

}