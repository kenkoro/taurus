package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType

class LoginResponseHandler : ResponseHandler {
  override suspend fun handle(
    subject: String,
    password: String,
    context: Context,
    loginViewModel: LoginViewModel,
  ): LoginResponseType {
    return if (subject.isNotBlank() && password.isNotBlank()) {
      loginViewModel.loginAndGetLoginResponseType(
        request =
          LoginRequest(
            subject = subject,
            password = password,
          ),
        context = context,
      )
    } else {
      LoginResponseType.BAD_CREDENTIALS
    }
  }
}