package com.kenkoro.taurus.mobile_client.feature_login.data.repository

import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.UserEntity
import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.dao.UserDao
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.UserRepository

class UserRepositoryImpl(
  private val userDao: UserDao
) :
  UserRepository {
  override suspend fun upsert(user: UserEntity) {
    return userDao.upsert(user)
  }

  override suspend fun delete(user: UserEntity) {
    return userDao.delete(user)
  }

  override suspend fun getUserById(id: Int): UserEntity? {
    return userDao.getUserById(id)
  }

  override suspend fun getUsers(): List<UserEntity> {
    return userDao.getUsers()
  }
}