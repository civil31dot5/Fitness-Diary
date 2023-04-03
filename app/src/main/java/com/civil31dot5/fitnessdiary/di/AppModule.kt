package com.civil31dot5.fitnessdiary.di

import com.civil31dot5.fitnessdiary.data.StravaAccountManagerImpl
import com.civil31dot5.fitnessdiary.domain.usecase.sport.StravaAccountManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindStravaAccountManager(stravaAccountManagerImpl: StravaAccountManagerImpl): StravaAccountManager

}