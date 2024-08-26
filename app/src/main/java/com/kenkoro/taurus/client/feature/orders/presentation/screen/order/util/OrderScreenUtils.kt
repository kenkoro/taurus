package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.LoginState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.profile.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

data class OrderScreenUtils(
  val ordersPagingFlow: Flow<PagingData<Order>>,
  val user: User? = null,
  val loginState: LoginState,
  val network: NetworkStatus,
  val selectedOrderRecordId: Int? = null,
  val saveUser: (User) -> Unit = {},
  val newOrdersFilter: (OrderFilterStrategy?) -> Unit = {},
  val selectOrder: (Int?) -> Unit = {},
  val newLoginState: (LoginState) -> Unit = {},
  val encryptJWToken: (String) -> Unit = {},
  val decryptUserSubjectAndItsPassword: () -> Pair<String, String>,
  val decryptJWToken: () -> String,
  val resetAllOrderStates: () -> Unit = {},
  val saveOrderStatus: (OrderStatus) -> Unit = {},
  val saveOrderId: (Int) -> Unit = {},
  val viewModelScope: CoroutineScope,
)