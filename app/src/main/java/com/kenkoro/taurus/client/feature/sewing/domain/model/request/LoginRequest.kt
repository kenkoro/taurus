package com.kenkoro.taurus.client.feature.sewing.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
  val subject: String,
  val password: String,
)