package com.civil31dot5.fitnessdiary.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.civil31dot5.fitnessdiary.data.database.RecordDao
import com.civil31dot5.fitnessdiary.data.database.RecordDatabase
import com.civil31dot5.fitnessdiary.data.repository.RecordRepositoryImpl
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun provideRecordRepository(
        repository: RecordRepositoryImpl
    ): RecordRepository

    companion object {
        @Singleton
        @Provides
        fun provideRecordDatabase(@ApplicationContext context: Context): RecordDatabase {
            return Room.databaseBuilder(
                context,
                RecordDatabase::class.java,
                "fitness_diary_database"
            ).build()
        }

        @Singleton
        @Provides
        fun provideRecordDao(database: RecordDatabase): RecordDao {
            return database.getRecordDao()
        }
    }
}