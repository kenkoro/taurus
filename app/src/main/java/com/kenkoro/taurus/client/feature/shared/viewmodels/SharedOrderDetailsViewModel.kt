package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.CategoryState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.ColorState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.CustomerState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.ModelState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.OrderDetails
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.QuantityState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.SizeState
import com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states.TitleState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedOrderDetailsViewModel
  @Inject
  constructor() : ViewModel() {
    var orderId by mutableIntStateOf(1)
      private set

    var date by mutableLongStateOf(0L)
      private set

    var status by mutableStateOf(OrderStatus.Idle)

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

    fun resetAllOrderDetails() {
      orderId = 1
      date = 0
      status = OrderStatus.Idle
      customer = CustomerState("")
      title = TitleState("")
      model = ModelState("")
      size = SizeState("")
      color = ColorState("")
      category = CategoryState("")
      quantity = QuantityState(null)
    }

    fun changeOrderId(orderId: Int) {
      this.orderId = orderId
    }

    fun changeDate(date: Long) {
      this.date = date
    }

    fun changeOrderStatus(status: OrderStatus) {
      this.status = status
    }

    fun getDetails(): OrderDetails {
      return OrderDetails(
        orderIdState = orderId,
        dateState = date,
        statusState = status,
        categoryState = category,
        colorState = color,
        customerState = customer,
        modelState = model,
        quantityState = quantity,
        sizeState = size,
        titleState = title,
      )
    }
  }