package com.kenkoro.taurus.client.feature.sewing.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingSourceFactory
import androidx.paging.PagingState
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepository
import io.ktor.client.call.body
import kotlinx.serialization.json.Json

class OrderPagingSourceFactory(
  private val orderRepository: OrderRepository,
  private val perPage: Int
) : PagingSourceFactory<Int, Order> {
  override fun invoke(): PagingSource<Int, Order> {
    return OrderPagingSource(orderRepository, perPage)
  }
}

private class OrderPagingSource(
  private val orderRepository: OrderRepository,
  private val perPage: Int
) : PagingSource<Int, Order>() {
  override fun getRefreshKey(state: PagingState<Int, Order>): Int? = null

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
    val page = params.key ?: 1
    return try {
      val orders = orderRepository.getOrders(page, perPage).run {
        Json.decodeFromString<List<Order>>(this.body() ?: "")
      }

      val nextPage = if (orders.isEmpty()) null else page + 1
      LoadResult.Page(
        data = orders,
        prevKey = null,
        nextKey = nextPage
      )
    } catch (e: Exception) {
      LoadResult.Error(e)
    }
  }
}