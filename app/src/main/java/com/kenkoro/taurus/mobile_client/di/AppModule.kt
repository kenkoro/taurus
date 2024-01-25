package com.kenkoro.taurus.mobile_client.di

import android.app.Application
import androidx.room.Room
import com.kenkoro.taurus.mobile_client.feature_login.data.repository.DummyApiRepository
import com.kenkoro.taurus.mobile_client.feature_login.data.repository.UserRepositoryImpl
import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.TaurusDatabase
import com.kenkoro.taurus.mobile_client.feature_login.data.source.remote.DummyApi
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.ApiRepository
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideTaurusDatabase(app: Application): TaurusDatabase {
    return Room.databaseBuilder(
      app,
      TaurusDatabase::class.java,
      TaurusDatabase.DB_NAME
    ).build()
  }

  @Provides
  @Singleton
  fun provideUserRepository(db: TaurusDatabase): UserRepository {
    return UserRepositoryImpl(db.userDao)
  }

  @Provides
  @Singleton
  fun provideDummyApiRepository(): ApiRepository {
    return DummyApiRepository(
      configRetrofitClientAndGetApi()
    )
  }

  private fun configRetrofitClientAndGetApi(): DummyApi {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(DummyApi.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create<DummyApi>()
  }
}