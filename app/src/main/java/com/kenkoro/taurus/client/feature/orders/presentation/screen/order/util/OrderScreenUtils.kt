package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

data class OrderScreenUtils(
  val ordersPagingFlow: Flow<PagingData<Order>>,
  val user: User? = null,
  val authStatus: AuthStatus,
  val network: NetworkStatus,
  val selectedOrderRecordId: Int? = null,
  val saveUser: (User) -> Unit = {},
  val newOrdersFilter: (OrderFilterStrategy?) -> Unit = {},
  val selectOrder: (Int?) -> Unit = {},
  val newLoginState: (AuthStatus) -> Unit = {},
  val encryptJWToken: (String) -> Unit = {},
  val decryptUserSubjectAndItsPassword: () -> Pair<String, String>,
  val decryptJWToken: () -> String,
  val resetAllOrderDetails: () -> Unit = {},
  val saveOrderStatus: (OrderStatus) -> Unit = {},
  val saveOrderId: (Int) -> Unit = {},
  val saveDate: (Long) -> Unit = {},
  val viewModelScope: CoroutineScope,
)