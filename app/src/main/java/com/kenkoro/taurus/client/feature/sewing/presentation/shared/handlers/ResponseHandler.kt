package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType

interface ResponseHandler {
  suspend fun handle(
    subject: String,
    password: String,
    encryptSubjectAndPassword: Boolean,
    login: suspend (String, String, Boolean) -> LoginResponseType,
  ): LoginResponseType
}