package com.kenkoro.taurus.client.core.crypto

import android.content.Context

class DecryptedCredentialService(private val context: Context) {
  fun storedSubject(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentialFilenames.SUBJECT_FILENAME,
      context = context,
    )
  }

  fun storedPassword(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentialFilenames.PASSWORD_FILENAME,
      context = context,
    )
  }

  fun storedToken(): String {
    return DecryptedCredential.decrypt(
      filename = LocalCredentialFilenames.TOKEN_FILENAME,
      context = context,
    )
  }

  fun deleteAll(): Boolean {
    return DecryptedCredential.deleteAll(context)
  }
}