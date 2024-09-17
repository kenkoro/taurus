package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.shared.states.AuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedAuthViewModel
  @Inject
  constructor() : ViewModel() {
    var authStatus by mutableStateOf(AuthStatus.WaitingForAuth)
      private set

    fun proceedAuth(authStatus: AuthStatus) {
      this.authStatus = authStatus
    }

    fun reset() {
      authStatus = AuthStatus.WaitingForAuth
    }
  }