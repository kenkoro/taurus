package com.kenkoro.taurus.client.feature.sewing.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepository
import io.ktor.client.call.body
import kotlinx.serialization.json.Json

@OptIn(ExperimentalPagingApi::class)
class OrderRemoteMediator(
  private val orderRepository: OrderRepository
) : RemoteMediator<Int, Order>() {
  override suspend fun load(loadType: LoadType, state: PagingState<Int, Order>): MediatorResult {
    return try {
      val page = when (loadType) {
        LoadType.REFRESH -> 1
        LoadType.PREPEND -> {
          return MediatorResult.Success(endOfPaginationReached = true)
        }

        LoadType.APPEND -> {
          val lastItem = state.lastItemOrNull()
          if (lastItem != null) {
            state.pages.size + 1
          } else {
            1
          }
        }
      }

      val orders = orderRepository.getOrders(page, state.config.pageSize).run {
        Json.decodeFromString<List<Order>>(this.body() ?: "")
      }
      MediatorResult.Success(endOfPaginationReached = orders.isEmpty())
    } catch (e: Exception) {
      MediatorResult.Error(e)
    }
  }
}