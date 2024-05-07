package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto

import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class OrderDto(val order: Order)