package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserColumnRequest(
  val value: String,
)