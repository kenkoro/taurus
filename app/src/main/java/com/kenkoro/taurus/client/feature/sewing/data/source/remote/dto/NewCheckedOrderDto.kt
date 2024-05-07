package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewCheckedOrderDto(
  @SerialName("cut_order_id") val cutOrderId: Int,
  @SerialName("checker_id") val checkerId: Int,
  val date: Long,
  val comment: String,
)