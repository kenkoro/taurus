package com.kenkoro.taurus.mobile_client.feature_login.domain.repository

import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.UserEntity

interface UserRepository {
  suspend fun upsert(user: UserEntity)

  suspend fun delete(user: UserEntity)

  suspend fun getUserById(id: Int): UserEntity?

  suspend fun getUsers(): List<UserEntity>
}