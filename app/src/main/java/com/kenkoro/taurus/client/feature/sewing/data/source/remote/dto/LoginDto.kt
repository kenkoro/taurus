package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
  val subject: String,
  val password: String,
)