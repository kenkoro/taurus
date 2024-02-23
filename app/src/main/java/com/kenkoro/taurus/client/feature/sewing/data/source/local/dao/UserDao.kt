package com.kenkoro.taurus.client.feature.sewing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kenkoro.taurus.client.feature.sewing.data.source.local.UserEntity

@Dao
interface UserDao {
  @Upsert
  suspend fun upsert(user: UserEntity)

  @Delete
  suspend fun delete(user: UserEntity)

  @Query("select * from userentity where id = :id")
  suspend fun getUserById(id: Int): UserEntity?

  @Query("select * from userentity")
  suspend fun getUsers(): List<UserEntity>
}