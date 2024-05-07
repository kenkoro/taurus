package com.kenkoro.taurus.client.feature.sewing.data.source.remote.paging

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService

@OptIn(ExperimentalPagingApi::class)
class OrderRemoteMediator(
  private val localDb: LocalDatabase,
  private val orderRepository: OrderRepositoryImpl,
  private val context: Context,
  private var page: Int = 1,
) : RemoteMediator<Int, OrderEntity>() {
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, OrderEntity>,
  ): MediatorResult {
    return try {
      val page =
        when (loadType) {
          LoadType.REFRESH -> 1
          LoadType.PREPEND -> {
            return MediatorResult.Success(endOfPaginationReached = true)
          }

          LoadType.APPEND -> this.page
        }

      val credentialService = DecryptedCredentialService(context)
      val (orders, hasNextPage) = orderRepository.getPaginatedOrders(
        page = page,
        perPage = state.config.pageSize,
        token = credentialService.storedToken(),
      ).getOrThrow()

      if (hasNextPage) {
        this.page++
      }

      localDb.withTransaction {
        if (loadType == LoadType.REFRESH) {
          localDb.orderDao.deleteAll()
        }

        val orderEntities =
          orders.map { orderDto ->
            orderDto.toOrderEntity()
          }
        localDb.orderDao.upsertAll(orderEntities)
      }
      MediatorResult.Success(endOfPaginationReached = !hasNextPage)
    } catch (e: Exception) {
      MediatorResult.Error(e)
    }
  }
}