package com.kenkoro.taurus.client.feature.sewing.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import com.kenkoro.taurus.client.feature.sewing.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
  @Inject
  constructor() : ViewModel() {
    private val _loginResponseType = mutableStateOf(LoginResponseType.SUCCESS)
    val loginResponseType = _loginResponseType

    fun saveLoginResponseType(loginResponseType: LoginResponseType) {
      _loginResponseType.value = loginResponseType
    }

    fun startDestination(): Screen {
      return if (
        _loginResponseType.value == LoginResponseType.BAD_DECRYPTED_CREDENTIALS ||
        _loginResponseType.value == LoginResponseType.FAILURE
      ) {
        Screen.LoginScreen
      } else {
        Screen.DashboardScreen
      }
    }
  }