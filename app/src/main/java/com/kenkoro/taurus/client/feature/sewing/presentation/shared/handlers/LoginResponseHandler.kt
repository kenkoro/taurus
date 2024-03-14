package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType

class LoginResponseHandler : ResponseHandler {
  override suspend fun handle(
    subject: String,
    password: String,
    encryptSubjectAndPassword: Boolean,
    login: suspend (String, String, Boolean) -> LoginResponseType,
  ): LoginResponseType {
    return if (subject.isNotBlank() && password.isNotBlank()) {
      login(subject, password, encryptSubjectAndPassword)
    } else {
      LoginResponseType.BadCredentials
    }
  }
}