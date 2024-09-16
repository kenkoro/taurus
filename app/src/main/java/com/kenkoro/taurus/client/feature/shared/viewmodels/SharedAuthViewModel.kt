package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.presentation.screen.order.states.AuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedAuthViewModel
  @Inject
  constructor(
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    var authStatus by mutableStateOf(AuthStatus.WaitingForAuth)
      private set
  }