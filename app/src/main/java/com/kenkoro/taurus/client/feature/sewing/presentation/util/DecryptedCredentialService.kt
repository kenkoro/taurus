package com.kenkoro.taurus.client.feature.sewing.presentation.util

import android.content.Context
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.JwtToken

class DecryptedCredentialService(private val context: Context) {
  fun storedSubject(): String {
    return DecryptedCredential.get(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  }

  fun storedPassword(): String {
    return DecryptedCredential.get(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value
  }

  fun storedToken(): JwtToken {
    return JwtToken(
      DecryptedCredential.get(
        filename = LocalCredentials.TOKEN_FILENAME,
        context = context,
      ).value,
    )
  }
}