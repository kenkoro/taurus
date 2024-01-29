package com.kenkoro.taurus.client.feature.login.domain.model

import com.kenkoro.taurus.client.feature.login.util.UserRole

data class User(
  val id: Int,
  val username: String,
  val firstName: String,
  val lastName: String,
  val role: UserRole
)