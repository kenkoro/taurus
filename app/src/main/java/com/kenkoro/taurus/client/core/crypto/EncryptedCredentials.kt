package com.kenkoro.taurus.client.core.crypto

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object EncryptedCredentials {
  fun encrypt(
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
}