package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

suspend fun remotelyGetUserWithLocallyScopedCredentials(
  context: Context,
  getUser: suspend (String, String) -> Unit,
) {
  val scope = CoroutineScope(Dispatchers.IO)
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

  scope.launch {
    getUser(firstName, token)
  }
}

suspend fun remotelyDeleteOrderWithLocallyScopedCredentials(
  context: Context,
  orderId: Int,
  deleterSubject: String,
  deleteOrder: suspend (Int, String, String) -> Unit,
) {
  val scope = CoroutineScope(Dispatchers.IO)
  val token =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value

  scope.launch {
    deleteOrder(orderId, token, deleterSubject)
  }
}

suspend fun loginWithLocallyScopedCredentials(
  login: suspend (String, String, Boolean) -> LoginResponse,
  context: Context,
): LoginResponse {
  val scope = CoroutineScope(Dispatchers.IO)
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
    scope.async {
      login(locallyStoredSubject, locallyStoredPassword, false)
    }.await()
  } else {
    LoginResponse.BadCredentials
  }
}