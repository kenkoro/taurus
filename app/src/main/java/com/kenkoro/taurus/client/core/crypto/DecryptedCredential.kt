package com.kenkoro.taurus.client.core.crypto

import android.content.Context
import java.io.File
import java.io.FileInputStream

object DecryptedCredential {
  fun decrypt(
    filename: String,
    context: Context,
  ): String {
    val cryptoManager = CryptoManager()
    val file = File(context.filesDir, "$filename.txt")
    if (!file.exists()) {
      return ""
    }
    val fis = FileInputStream(file)
    return cryptoManager.decrypt(fis).decodeToString()
  }
}