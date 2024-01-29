package com.kenkoro.taurus.client.feature.login.domain.repository

import com.kenkoro.taurus.client.feature.login.data.source.local.UserEntity

interface UserRepository {
  suspend fun upsert(user: UserEntity)

  suspend fun delete(user: UserEntity)

  suspend fun getUserById(id: Int): UserEntity?

  suspend fun getUsers(): List<UserEntity>
}