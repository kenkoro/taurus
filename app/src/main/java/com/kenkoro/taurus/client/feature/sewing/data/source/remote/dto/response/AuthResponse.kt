package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
  val token: String,
)