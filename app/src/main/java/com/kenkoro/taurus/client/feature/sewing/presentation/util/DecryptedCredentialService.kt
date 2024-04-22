package com.kenkoro.taurus.client.feature.sewing.presentation.util

import android.content.Context

class DecryptedCredentialService(private val context: Context) {
  fun storedSubject(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  }

  fun storedPassword(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    ).value
  }

  fun storedToken(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value
  }
}