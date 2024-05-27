package com.kenkoro.taurus.client.feature.orders.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kenkoro.taurus.client.feature.orders.domain.OrderStatus

@Entity(
  tableName = "order_entities",
)
data class OrderEntity(
  @PrimaryKey(autoGenerate = true) @ColumnInfo("record_id") val recordId: Int = 0,
  @ColumnInfo("order_id") val orderId: Int,
  val customer: String,
  val date: Long,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
  @ColumnInfo("creator_id") val creatorId: Int,
)