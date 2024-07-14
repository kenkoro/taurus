package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.mappers.toNewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.QuantityState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.SizeState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.TitleState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditorViewModel
  @Inject
  constructor(
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    var orderId by mutableIntStateOf(1)
      private set

    var customer by mutableStateOf(CustomerState(customer = ""))
      private set

    var title by mutableStateOf(TitleState(title = ""))
      private set

    var model by mutableStateOf(ModelState(model = ""))
      private set

    var size by mutableStateOf(SizeState(size = ""))
      private set

    var color by mutableStateOf(ColorState(color = ""))
      private set

    var category by mutableStateOf(CategoryState(category = ""))
      private set

    var quantity by mutableStateOf(QuantityState(quantity = null))
      private set

    var status by mutableStateOf(OrderStatus.Idle)
      private set

    fun resetAll() {
      customer = CustomerState(customer = "")
      title = TitleState(title = "")
      model = ModelState(model = "")
      size = SizeState(size = "")
      color = ColorState(color = "")
      category = CategoryState(category = "")
      quantity = QuantityState(quantity = null)
    }

    fun status(status: OrderStatus) {
      this.status = status
    }

    fun orderId(orderId: Int) {
      this.orderId = orderId
    }

    suspend fun editOrderRemotely(
      dto: NewOrder,
      orderId: Int,
      editorSubject: String,
    ): Boolean {
      val result =
        orderRepository.editOrder(
          dto = dto.toNewOrderDto(),
          orderId = orderId,
          editorSubject = editorSubject,
          token = decryptedCredentialService.storedToken(),
        )

      return result.isSuccess
    }
  }