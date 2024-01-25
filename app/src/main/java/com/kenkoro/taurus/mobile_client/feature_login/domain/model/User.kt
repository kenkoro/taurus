package com.kenkoro.taurus.mobile_client.feature_login.domain.model

import com.kenkoro.taurus.mobile_client.feature_login.core.util.UserRole

data class User(
  val id: Int,
  val username: String,
  val firstName: String,
  val lastName: String,
  val role: UserRole
)

class InvalidUserException(message: String): Exception(message)