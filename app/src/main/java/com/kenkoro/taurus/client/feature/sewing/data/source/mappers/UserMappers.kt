package com.kenkoro.taurus.client.feature.sewing.data.source.mappers

import com.kenkoro.taurus.client.feature.sewing.data.source.local.UserEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.UserDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.User

fun UserDto.toUserEntity(): UserEntity =
  UserEntity(
    userId = userId,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt,
  )

fun User.toUserDto(): UserDto =
  UserDto(
    userId = userId,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt,
  )

fun User.toUserEntity(): UserEntity =
  UserEntity(
    userId = userId,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt,
  )

fun UserEntity.toUser(): User =
  User(
    userId = userId,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt,
  )

fun UserDto.toUser(): User =
  User(
    userId = userId,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt,
  )