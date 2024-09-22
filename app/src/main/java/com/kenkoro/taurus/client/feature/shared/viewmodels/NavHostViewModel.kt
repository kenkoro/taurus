package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel
  @Inject
  constructor(
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    fun decryptUserCredentials(): Pair<String, String> {
      return decryptedCredentialService.decryptUserCredentials()
    }
  }