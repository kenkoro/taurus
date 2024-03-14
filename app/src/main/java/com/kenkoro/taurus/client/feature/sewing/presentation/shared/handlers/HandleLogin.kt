package com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun handleLogin(
  login: suspend (String, String, Boolean) -> LoginResponseType,
  onResponse: (LoginResponseType) -> Unit,
  onDashboardNavigate: () -> Unit,
  scope: CoroutineScope,
  context: Context,
) {
  val handler: ResponseHandler = LoginResponseHandler()
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

  if (locallyStoredSubject.isNotBlank() && locallyStoredPassword.isNotBlank()) {
    scope.launch {
      handler.handle(
        subject = locallyStoredSubject,
        password = locallyStoredPassword,
        encryptSubjectAndPassword = false,
        login = login,
      ).run {
        onResponse(this)
      }
    }
  } else {
    onDashboardNavigate()
  }
}