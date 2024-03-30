package com.kenkoro.taurus.client.feature.sewing.data.source.mappers

import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.domain.model.User

fun GetUserResponseDto.toUser(): User {
  return User(
    id = id,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
    salt = salt
  )
}