package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
  @Inject
  constructor() : ViewModel() {
    private val _loginResponseType = mutableStateOf(LoginResponseType.Pending)
    val loginResponseType = _loginResponseType

    fun onResponse(response: LoginResponseType) {
      _loginResponseType.value = response
    }
  }