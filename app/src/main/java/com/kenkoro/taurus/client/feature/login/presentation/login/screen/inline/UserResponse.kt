package com.kenkoro.taurus.client.feature.login.presentation.login.screen.inline

import com.kenkoro.taurus.client.feature.login.domain.model.User

@JvmInline
value class UserResponse(private val user: User) {
  fun isOk(): Boolean {
    return user.firstName.isNotBlank()
  }
}