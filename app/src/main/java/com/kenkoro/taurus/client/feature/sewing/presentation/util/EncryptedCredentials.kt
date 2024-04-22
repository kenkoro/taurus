package com.kenkoro.taurus.client.feature.sewing.presentation.util

import android.content.Context
import com.kenkoro.taurus.client.core.crypto.CryptoManager
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.LoginCredentials
import java.io.File
import java.io.FileOutputStream

object EncryptedCredentials {
  fun encryptCredential(
    credential: String,
    filename: String,
    context: Context,
  ) {
    val bytes = credential.encodeToByteArray()
    val cryptoManager = CryptoManager()
    val file = File(context.filesDir, "$filename.txt")
    if (!file.exists()) {
      file.createNewFile()
    }
    val fos = FileOutputStream(file)
    cryptoManager.encrypt(
      bytes = bytes,
      outputStream = fos,
    )
  }

  fun encryptCredentials(
    credentials: LoginCredentials,
    context: Context,
  ) {
    encryptCredential(
      credential = credentials.subject,
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    )
    encryptCredential(
      credential = credentials.password,
      filename = LocalCredentials.PASSWORD_FILENAME,
      context = context,
    )
    encryptCredential(
      credential = credentials.token,
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    )
  }
}