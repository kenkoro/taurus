package com.kenkoro.taurus.client.feature.sewing.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
  val token: String
)