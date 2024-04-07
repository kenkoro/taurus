package com.kenkoro.taurus.client.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.OrderKtorApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserKtorApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.paging.OrderRemoteMediator
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepository
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepositoryImpl
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

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  private val client =
    HttpClient(CIO) {
      install(Logging) {
        level = LogLevel.ALL
      }
      install(ContentNegotiation) {
        json()
      }
    }

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
      userApi = UserKtorApi(client),
    )
  }

  @Provides
  @Singleton
  fun provideOrderRepository(): OrderRepositoryImpl {
    return OrderRepository.create(
      orderApi = OrderKtorApi(client),
    )
  }

  @Provides
  @Singleton
  fun provideOrderPager(
    app: Application,
    localDb: LocalDatabase,
    orderRepository: OrderRepositoryImpl,
  ): Pager<Int, OrderEntity> {
    val pageSize = 25
    return Pager(
      config = PagingConfig(pageSize = pageSize),
      remoteMediator =
        OrderRemoteMediator(
          localDb = localDb,
          orderRepository = orderRepository,
          context = app,
        ),
      pagingSourceFactory = {
        localDb.orderDao.pagingSource()
      },
    )
  }
}