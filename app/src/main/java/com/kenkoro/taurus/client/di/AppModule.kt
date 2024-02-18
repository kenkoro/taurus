package com.kenkoro.taurus.client.di

import android.app.Application
import androidx.room.Room
import com.kenkoro.taurus.client.feature.login.data.source.local.LocalDatabase
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
      LocalDatabase.DB_NAME
    ).build()
  }
}