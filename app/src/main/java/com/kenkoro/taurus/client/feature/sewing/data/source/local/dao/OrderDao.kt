package com.kenkoro.taurus.client.feature.sewing.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity

@Dao
interface OrderDao {
  @Upsert
  suspend fun upsert(orderEntity: OrderEntity)

  @Upsert
  suspend fun upsertAll(orderEntities: List<OrderEntity>)

  @Delete
  suspend fun delete(orderEntity: OrderEntity)

  @Query("delete from order_entities")
  suspend fun deleteAll()

  @Query("select * from order_entities where order_id = :orderId")
  suspend fun getOrderById(orderId: Int): OrderEntity?

  @Query("select * from order_entities")
  fun pagingSource(): PagingSource<Int, OrderEntity>
}