package com.kenkoro.taurus.client.feature.sewing.presentation.screen.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
  @Inject
  constructor() : ViewModel() {
    var loginResponseType by mutableStateOf(LoginResponseType.Pending)

    fun onResponse(response: LoginResponseType) {
      loginResponseType = response
    }
  }