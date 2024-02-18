package com.kenkoro.taurus.client.feature.login.presentation.login.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
  private val _subject = mutableStateOf("")
  val subject: MutableState<String> = _subject

  private val _password = mutableStateOf("")
  val password: MutableState<String> = _password
}