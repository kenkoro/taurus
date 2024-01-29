package com.kenkoro.taurus.client.feature.login.domain.model

data class AuthRequest(
  val username: String,
  val password: String
)