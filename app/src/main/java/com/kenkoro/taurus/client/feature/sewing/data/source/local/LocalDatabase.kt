package com.kenkoro.taurus.client.feature.sewing.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenkoro.taurus.client.feature.sewing.data.source.local.dao.OrderDao
import com.kenkoro.taurus.client.feature.sewing.data.source.local.dao.UserDao

@Database(
  entities = [UserEntity::class, OrderEntity::class],
  version = 3,
)
abstract class LocalDatabase : RoomDatabase() {
  abstract val userDao: UserDao
  abstract val orderDao: OrderDao

  companion object {
    const val DB_NAME = "local.db"
  }
}