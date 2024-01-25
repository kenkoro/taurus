package com.kenkoro.taurus.mobile_client.feature_login.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.dao.UserDao

@Database(
  entities = [UserEntity::class],
  version = 1
)
abstract class TaurusDatabase : RoomDatabase() {
  abstract val userDao: UserDao

  companion object {
    const val DB_NAME = "taurus.db"
  }
}