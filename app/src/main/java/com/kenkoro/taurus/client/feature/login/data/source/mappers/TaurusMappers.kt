package com.kenkoro.taurus.client.feature.login.data.source.mappers

import com.kenkoro.taurus.client.feature.login.data.source.local.UserEntity
import com.kenkoro.taurus.client.feature.login.data.source.remote.UserDto
import com.kenkoro.taurus.client.feature.login.domain.model.User
import com.kenkoro.taurus.client.feature.login.util.UserRole

/**
 * WARN: Change user role when working w/ your own api.
 */
fun UserDto.toUserEntity(): UserEntity {
  return UserEntity(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    role = com.kenkoro.taurus.client.feature.login.util.UserRole.NONE
  )
}

fun UserEntity.toUser(): User {
  return User(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    role = UserRole.NONE
  )
}