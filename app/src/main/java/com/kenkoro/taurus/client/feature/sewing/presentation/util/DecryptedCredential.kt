package com.kenkoro.taurus.client.feature.sewing.presentation.util

import android.content.Context
import com.kenkoro.taurus.client.core.crypto.CryptoManager
import java.io.File
import java.io.FileInputStream

@JvmInline
value class DecryptedCredential(val value: String) {
  companion object {
    fun get(
      filename: String,
      context: Context,
    ): DecryptedCredential {
      val cryptoManager = CryptoManager()
      val file = File(context.filesDir, "$filename.txt")
      if (!file.exists()) {
        return DecryptedCredential("")
      }
      val fis = FileInputStream(file)
      return DecryptedCredential(cryptoManager.decrypt(fis).decodeToString())
    }
  }
}