package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order.states

import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewOrder
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

data class OrderDetails(
  val orderIdState: Int = 1,
  val dateState: Long = 0L,
  val statusState: OrderStatus = OrderStatus.Idle,
  val categoryState: TaurusTextFieldState = CategoryState(),
  val colorState: TaurusTextFieldState = ColorState(),
  val customerState: TaurusTextFieldState = CustomerState(),
  val modelState: TaurusTextFieldState = ModelState(),
  val quantityState: TaurusTextFieldState = QuantityState(),
  val sizeState: TaurusTextFieldState = SizeState(),
  val titleState: TaurusTextFieldState = TitleState(),
) {
  fun packEditOrder(
    status: OrderStatus,
    creatorId: Int,
  ): EditOrder {
    return EditOrder(
      orderId = orderIdState,
      customer = customerState.text,
      date = dateState,
      title = titleState.text,
      model = modelState.text,
      size = sizeState.text,
      color = colorState.text,
      category = categoryState.text,
      quantity = quantityState.text.toIntOrNull() ?: 0,
      status = status,
      creatorId = creatorId,
    )
  }

  fun packNewOrder(
    status: OrderStatus,
    creatorId: Int,
  ): NewOrder {
    return NewOrder(
      customer = customerState.text,
      date = dateState,
      title = titleState.text,
      model = modelState.text,
      size = sizeState.text,
      color = colorState.text,
      category = categoryState.text,
      quantity = quantityState.text.toIntOrNull() ?: 0,
      status = status,
      creatorId = creatorId,
    )
  }
}