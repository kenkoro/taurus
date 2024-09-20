package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util

import androidx.paging.PagingData
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.orders.domain.Order
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.util.filter.OrderFilterStrategy
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

data class OrderScreenUtils(
  val ordersPagingFlow: Flow<PagingData<Order>>,
  val selectedOrderRecordId: Int?,
  val filter: (strategy: OrderFilterStrategy?) -> Unit,
  val newOrderSelection: (recordId: Int) -> Unit,
  val clearOrderSelection: () -> Unit,
  val auth: suspend (subject: String, password: String) -> Result<TokenDto>,
  val decryptUserCredentials: () -> Pair<String, String>,
  val encryptJWToken: (tokenDto: TokenDto) -> Unit,
  val getUser: suspend (subject: String, tokenDto: TokenDto, postAction: () -> Unit) -> Boolean,
  val getActualQuantityOfCutMaterial: suspend (orderId: Int) -> Result<ActualCutOrdersQuantityDto>,
  val addNewCutOrder: suspend (cutOrder: NewCutOrder) -> Result<CutOrderDto>,
  val editOrder: suspend (dto: EditOrder, editor: String, postAction: () -> Unit) -> Boolean,
  val deleteOrder: suspend (order: Order, deleter: String, postAction: () -> Unit) -> Boolean,
  val scope: CoroutineScope,
)