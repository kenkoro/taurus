package com.kenkoro.taurus.client.feature.sewing.presentation.util

import android.content.Context
import com.kenkoro.taurus.client.core.crypto.CryptoManager
import java.io.File
import java.io.FileInputStream

@JvmInline
value class DecryptedCredentials(val value: String) {
  companion object {
    fun getDecryptedCredential(
      filename: String,
      context: Context,
    ): DecryptedCredentials {
      val cryptoManager = CryptoManager()
      val file = File(context.filesDir, "$filename.txt")
      if (!file.exists()) {
        return DecryptedCredentials("")
      }
      val fis = FileInputStream(file)
      return DecryptedCredentials(cryptoManager.decrypt(fis).decodeToString())
    }
  }
}