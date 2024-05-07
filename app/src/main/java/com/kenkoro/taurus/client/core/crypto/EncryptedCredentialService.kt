package com.kenkoro.taurus.client.core.crypto

import android.content.Context

class EncryptedCredentialService(private val context: Context) {
  fun putSubject(subject: String) {
    EncryptedCredentials.encrypt(
      credential = subject,
      filename = LocalCredentialFilenames.SUBJECT_FILENAME,
      context = context,
    )
  }

  fun putPassword(password: String) {
    EncryptedCredentials.encrypt(
      credential = password,
      filename = LocalCredentialFilenames.PASSWORD_FILENAME,
      context = context,
    )
  }

  fun putToken(token: String) {
    EncryptedCredentials.encrypt(
      credential = token,
      filename = LocalCredentialFilenames.TOKEN_FILENAME,
      context = context,
    )
  }
}