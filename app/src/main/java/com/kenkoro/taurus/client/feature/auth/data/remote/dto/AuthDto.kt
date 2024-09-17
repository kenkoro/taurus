package com.kenkoro.taurus.client.feature.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
  val subject: String,
  val password: String,
)