package com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.inline

import com.kenkoro.taurus.mobile_client.feature_login.domain.model.User

@JvmInline
value class UserResponse(private val user: User) {
  fun isOk(): Boolean {
    return user.firstName.isNotBlank()
  }
}