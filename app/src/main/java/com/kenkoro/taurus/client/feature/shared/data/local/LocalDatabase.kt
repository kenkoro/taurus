package com.kenkoro.taurus.client.feature.shared.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenkoro.taurus.client.feature.login.data.local.UserEntity
import com.kenkoro.taurus.client.feature.login.data.local.dao.UserDao
import com.kenkoro.taurus.client.feature.orders.data.local.OrderEntity
import com.kenkoro.taurus.client.feature.orders.data.local.dao.OrderDao

@Database(
  entities = [UserEntity::class, OrderEntity::class],
  version = 1,
)
abstract class LocalDatabase : RoomDatabase() {
  abstract val userDao: UserDao
  abstract val orderDao: OrderDao

  companion object {
    const val DB_NAME = "local.db"
  }
}