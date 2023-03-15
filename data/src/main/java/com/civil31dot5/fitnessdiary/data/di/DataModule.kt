package com.civil31dot5.fitnessdiary.data.di

import com.civil31dot5.fitnessdiary.data.repository.RecordRepositoryImpl
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}