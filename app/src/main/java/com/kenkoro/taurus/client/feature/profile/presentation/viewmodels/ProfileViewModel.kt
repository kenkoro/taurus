package com.kenkoro.taurus.client.feature.profile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
  @Inject
  constructor(
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    fun deleteAllUserCredentials(): Boolean = decryptedCredentialService.deleteAll()
  }