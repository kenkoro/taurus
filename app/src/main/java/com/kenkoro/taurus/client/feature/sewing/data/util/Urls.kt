package com.kenkoro.taurus.client.feature.sewing.data.util

object Urls {
  private const val PREFIX = "api"

  const val HOST = "ktor-backend.loca.lt"
  const val LOGIN = "$PREFIX/login"
  const val GET_USER = "$PREFIX/user"
  const val CREATE_USER = "$PREFIX/create/user"
  const val DELETE_USER = "$PREFIX/delete/user"
}