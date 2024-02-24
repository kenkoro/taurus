package com.kenkoro.taurus.client.di

import android.app.Application
import androidx.room.Room
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    return UserRepository.create()
  }
}