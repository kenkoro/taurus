package com.kenkoro.taurus.client.feature.sewing.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kenkoro.taurus.client.feature.sewing.data.util.OrderStatus

@Entity
data class OrderEntity(
  @PrimaryKey val orderId: Int,
  val customer: String,
  val date: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
)