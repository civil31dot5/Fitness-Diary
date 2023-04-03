package com.civil31dot5.fitnessdiary.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.civil31dot5.fitnessdiary.data.StravaAccountManagerImpl
import com.civil31dot5.fitnessdiary.data.database.RecordDao
import com.civil31dot5.fitnessdiary.data.database.RecordDatabase
import com.civil31dot5.fitnessdiary.data.database.StravaSportDao
import com.civil31dot5.fitnessdiary.data.network.StravaApi
import com.civil31dot5.fitnessdiary.data.network.StravaApiImpl
import com.civil31dot5.fitnessdiary.data.repository.RecordRepositoryImpl
import com.civil31dot5.fitnessdiary.data.repository.StravaRepositoryImpl
import com.civil31dot5.fitnessdiary.domain.repository.RecordRepository
import com.civil31dot5.fitnessdiary.domain.repository.StravaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.prefs.Preferences
import javax.inject.Qualifier
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRecordRepository(repository: RecordRepositoryImpl): RecordRepository {
        return repository
    }

    @Singleton
    @Provides
    fun provideStravaRepository(repository: StravaRepositoryImpl): StravaRepository {
        return repository
    }

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

    @Singleton
    @Provides
    fun provideStravaSportDao(database: RecordDatabase): StravaSportDao{
        return database.getStravaSportDao()
    }


    @Singleton
    @Provides
    fun provideHttpClient(
        stravaAccountManager: StravaAccountManagerImpl,
        @StravaApiBaseUrl stravaBaseUrl: String
    ): HttpClient{
        val client = HttpClient(OkHttp){
            defaultRequest {
                url(stravaBaseUrl)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("loggerTag").d(message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(Auth){
                bearer {
                    loadTokens {
                        BearerTokens(stravaAccountManager.getAccessToken(), stravaAccountManager.getRefreshToken())
                    }

                    refreshTokens {
                        stravaAccountManager.refreshToken()
                        BearerTokens(stravaAccountManager.getAccessToken(), stravaAccountManager.getRefreshToken())
                    }

                }
            }
        }

        return client
    }

    @StravaApiBaseUrl
    @Provides
    fun provideStravaApiBaseUrl(): String{
        return "https://www.strava.com/api/v3/"
    }

    @Singleton
    @Provides
    fun provideStravaApi(impl: StravaApiImpl): StravaApi{
        return impl
    }

    @SecretSharedPrefs
    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences{
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
           "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StravaApiBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecretSharedPrefs