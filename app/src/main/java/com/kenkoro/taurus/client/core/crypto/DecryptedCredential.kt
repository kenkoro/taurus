package com.kenkoro.taurus.client.core.crypto

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException

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

  fun deleteAll(context: Context): Boolean {
    val subjectFile = File(context.filesDir, "${LocalCredentialFilenames.SUBJECT_FILENAME}.txt")
    val passwordFile = File(context.filesDir, "${LocalCredentialFilenames.PASSWORD_FILENAME}.txt")
    val tokenFile = File(context.filesDir, "${LocalCredentialFilenames.TOKEN_FILENAME}.txt")
    try {
      val subjectResult = subjectFile.delete()
      val passwordResult = passwordFile.delete()
      val tokenResult = tokenFile.delete()

      return subjectResult && passwordResult && tokenResult
    } catch (ioe: IOException) {
      Log.d("kenkoro", ioe.message!!)
      return false
    }
  }
}