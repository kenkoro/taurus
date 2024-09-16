package com.kenkoro.taurus.client.feature.auth.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.auth.presentation.util.PasswordState
import com.kenkoro.taurus.client.feature.auth.presentation.util.SubjectState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthTextFieldsViewModel
  @Inject
  constructor() : ViewModel() {
    var subject by mutableStateOf(SubjectState(subject = ""))
      private set

    var password by mutableStateOf(PasswordState(password = ""))
      private set

    fun showErrorTitle(): Boolean {
      return subject.showErrors() || password.showErrors()
    }
  }