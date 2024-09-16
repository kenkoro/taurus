package com.kenkoro.taurus.client.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.core.crypto.EncryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.remote.api.LoginRemoteApiImpl
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.LoginRepository
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.LoginRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.OrderRemoteMediator
import com.kenkoro.taurus.client.feature.orders.data.remote.api.CutOrderRemoteApiImpl
import com.kenkoro.taurus.client.feature.orders.data.remote.api.OrderRemoteApiImpl
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepository
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepository
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.profile.data.remote.api.UserRemoteApiImpl
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepository
import com.kenkoro.taurus.client.feature.profile.data.remote.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
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
object TestAppModule {
  private val client =
    HttpClient(CIO) {
      developmentMode = true
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
    return Room.inMemoryDatabaseBuilder(
      app,
      LocalDatabase::class.java,
    ).build()
  }

  @Provides
  @Singleton
  fun provideLoginRepository(): LoginRepositoryImpl {
    return LoginRepository.create(LoginRemoteApiImpl(client))
  }

  @Provides
  @Singleton
  fun provideUserRepository(): UserRepositoryImpl {
    return UserRepository.create(UserRemoteApiImpl(client))
  }

  @Provides
  @Singleton
  fun provideOrderRepository(): OrderRepositoryImpl {
    return OrderRepository.create(OrderRemoteApiImpl(client))
  }

  @Provides
  @Singleton
  fun provideCutOrderRepository(): CutOrderRepositoryImpl {
    return CutOrderRepository.create(CutOrderRemoteApiImpl(client))
  }

  @Provides
  @Singleton
  fun provideDecryptedCredentialService(app: Application): DecryptedCredentialService {
    return DecryptedCredentialService(app)
  }

  @Provides
  fun provideEncryptedCredentialService(app: Application): EncryptedCredentialService {
    return EncryptedCredentialService(app)
  }

  @Provides
  @Singleton
  fun provideOrderPager(
    localDb: LocalDatabase,
    orderRepository: OrderRepositoryImpl,
    decryptedCredentialService: DecryptedCredentialService,
  ): Pager<Int, OrderEntity> {
    val pageSize = 40
    return Pager(
      config = PagingConfig(pageSize = pageSize),
      remoteMediator =
        OrderRemoteMediator(
          localDb = localDb,
          orderRepository = orderRepository,
          decryptedCredentialService = decryptedCredentialService,
        ),
      pagingSourceFactory = {
        localDb.orderDao.pagingSource()
      },
    )
  }
}