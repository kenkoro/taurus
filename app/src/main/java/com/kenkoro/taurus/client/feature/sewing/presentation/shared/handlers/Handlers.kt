package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.OrderRequestDto
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse

suspend fun remotelyGetUserWithLocallyScopedCredentials(
  context: Context,
  getUser: suspend (String, String) -> Unit,
) {
  val firstName =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val token =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value

  getUser(firstName, token)
}

suspend fun remotelyDeleteOrderWithLocallyScopedCredentials(
  context: Context,
  orderId: Int,
  deleterSubject: String,
  deleteOrder: suspend (Int, String, String) -> Unit,
) {
  val token =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value

  deleteOrder(orderId, token, deleterSubject)
}

suspend fun remotelyCreateANewOrderWithLocallyScopedCredentials(
  context: Context,
  request: OrderRequestDto,
  newOrder: suspend (OrderRequestDto, String) -> Unit,
) {
  val token =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value

  newOrder(request, token)
}

suspend fun loginWithLocallyScopedCredentials(
  login: suspend (String, String, Boolean) -> LoginResponse,
  context: Context,
): LoginResponse {
  val locallyStoredSubject =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val locallyStoredPassword =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value

  return if (locallyStoredSubject.isNotBlank() && locallyStoredPassword.isNotBlank()) {
    login(locallyStoredSubject, locallyStoredPassword, false)
  } else {
    LoginResponse.BadCredentials
  }
}