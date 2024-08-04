package com.kenkoro.taurus.client.feature.shared

object Urls {
  private const val HOST = "192.168.0.112"

  private const val PROTOCOL = "http"
  private const val PORT = 8080
  private const val URL = "$PROTOCOL://$HOST:$PORT"

  const val LOGIN = "$URL/login"

  private const val ADD_NEW_TEMPLATE = "$URL/add-new"

  const val ADD_NEW_ORDER = "$ADD_NEW_TEMPLATE/order"
  const val GET_ORDER = "$URL/order"
  const val GET_PAGINATED_ORDERS = "$URL/orders"
  const val EDIT_ORDER = "$URL/edit/order"
  const val DELETE_ORDER = "$URL/delete/order"

  const val ADD_NEW_CUT_ORDER = "$ADD_NEW_TEMPLATE/cut-order"
  const val GET_ACTUAL_CUT_ORDERS_QUANTITY = "$GET_ORDER/actual-quantity"

  const val ADD_NEW_USER = "$URL/add-new/user"
  const val GET_USER = "$URL/user"
  const val EDIT_USER = "$URL/edit/user"
  const val DELETE_USER = "$URL/delete/user"
}