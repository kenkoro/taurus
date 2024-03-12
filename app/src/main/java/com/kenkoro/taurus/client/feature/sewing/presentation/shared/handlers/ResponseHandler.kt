package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType

interface ResponseHandler {
  suspend fun handle(
    subject: String,
    password: String,
    context: Context,
    loginViewModel: LoginViewModel,
  ): LoginResponseType
}