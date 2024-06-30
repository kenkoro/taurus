package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.util.OrderIdState
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
  ) : ViewModel() {
    var orderId by mutableStateOf(OrderIdState(orderId = null))
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

    fun resetAll() {
      orderId.text = ""
      customer.text = ""
      title.text = ""
      model.text = ""
      size.text = ""
      color.text = ""
      category.text = ""
      quantity.text = ""
    }

    suspend fun editOrderRemotely(
      dto: NewOrderDto,
      orderId: Int,
      editorSubject: String,
      token: String,
    ): Boolean {
      val result =
        orderRepository.editOrder(
          dto = dto,
          orderId = orderId,
          editorSubject = editorSubject,
          token = token,
        )

      return result.isSuccess
    }
  }