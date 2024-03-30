package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequestDto(
  val updater: String,
  val value: String,
)