package com.kenkoro.taurus.client.feature.sewing.data.source.remote.paging

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetOrdersResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

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
      val scope = CoroutineScope(Dispatchers.IO)
      val page =
        when (loadType) {
          LoadType.REFRESH -> 1
          LoadType.PREPEND -> {
            return MediatorResult.Success(endOfPaginationReached = true)
          }

          LoadType.APPEND -> this.page
        }

      val (orders, hasNextPage) =
        scope.async {
          val token =
            DecryptedCredentials.getDecryptedCredential(
              filename = LocalCredentials.TOKEN_FILENAME,
              context = context,
            ).value
          try {
            orderRepository
              .token(token)
              .getOrders(page, state.config.pageSize).body<GetOrdersResponseDto>().run {
                Pair(this.paginatedOrders, this.hasNextPage)
              }
          } catch (e: Exception) {
            Log.d("kenkoro", e.message!!)
            Pair(emptyList(), false)
          }
        }.await()

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