package com.kenkoro.taurus.client.feature.sewing.data.util

object Urls {
  private const val KTOR_LOCALHOST = "ktor-backend.loca.lt"
  private const val LOCALHOST = "10.10.65.179"
  private const val LOCALHOST_PHONE = "192.168.224.149"
  const val HOST = LOCALHOST_PHONE
  const val PORT = 8080

  object User {
    const val LOGIN = "login"
    const val GET_USER = "user"
  }

  object Order {
    const val GET_ORDER = "order"
    const val GET_ORDERS = "orders"
    const val NEW_ORDER = "new/order"
    const val DELETE_ORDER = "delete/order"
  }
}