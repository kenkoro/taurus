package com.kenkoro.taurus.mobile_client.feature_login.data.source.mappers

import com.kenkoro.taurus.mobile_client.feature_login.core.util.UserRole
import com.kenkoro.taurus.mobile_client.feature_login.data.source.local.UserEntity
import com.kenkoro.taurus.mobile_client.feature_login.data.source.remote.UserDto
import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User

/**
 * WARN: Change user role when working w/ your own api.
 */
fun UserDto.toUserEntity(): UserEntity {
  return UserEntity(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    role = UserRole.NONE
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