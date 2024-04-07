package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.kenkoro.taurus.client.feature.sewing.data.source.local.LocalDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrder
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.DeleteRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
@Inject
constructor(
  pager: Pager<Int, OrderEntity>,
  private val localDb: LocalDatabase,
  private val orderRepository: OrderRepositoryImpl,
) : ViewModel() {
  val orderPagingFlow =
    pager
      .flow
      .map { pagingData ->
        pagingData.map { it.toOrder() }
      }
      .flowOn(Dispatchers.IO)
      .cachedIn(viewModelScope)

  suspend fun deleteOrderLocally(order: Order) {
    localDb.withTransaction {
      localDb.orderDao.delete(order.toOrderEntity())
    }
  }

  suspend fun upsertOrderLocally(order: Order) {
    localDb.withTransaction {
      localDb.orderDao.upsert(order.toOrderEntity())
    }
  }

  suspend fun deleteOrderRemotely(
    orderId: Int,
    token: String,
    deleterSubject: String,
  ) {
    orderRepository.run {
      token(token)
      deleteOrder(orderId, DeleteRequestDto(deleterSubject))
    }
  }
}