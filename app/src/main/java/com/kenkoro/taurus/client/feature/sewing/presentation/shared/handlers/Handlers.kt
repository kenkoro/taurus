package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType

suspend fun handleUserGetWithLocallyScopedCredentials(
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

suspend fun handleLoginWithLocallyScopedCredentials(
  login: suspend (String, String, Boolean) -> LoginResponseType,
  context: Context,
): LoginResponseType {
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
    LoginResponseType.BadCredentials
  }
}