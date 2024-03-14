package com.kenkoro.taurus.client.di

import android.app.Application
import androidx.room.Room
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserKtorApi
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepository
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideLocalDatabase(app: Application): LocalDatabase {
    return Room.databaseBuilder(
      app,
      LocalDatabase::class.java,
      LocalDatabase.DB_NAME,
    ).build()
  }

  @Provides
  @Singleton
  fun provideUserRepository(): UserRepositoryImpl {
    return UserRepository.create(
      UserKtorApi(
        client =
          HttpClient(CIO) {
            install(Logging) {
              level = LogLevel.ALL
            }
            install(ContentNegotiation) {
              json()
            }
          },
      ),
    )
  }
}