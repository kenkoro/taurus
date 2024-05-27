package com.kenkoro.taurus.client.feature.login.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
  val subject: String,
  val password: String,
)