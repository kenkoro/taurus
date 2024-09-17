package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.profile.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(

  ) : ViewModel() {
  var user by mutableStateOf<User?>(null)
    private set

  fun saveUser(user: User) {
    this.user = user
  }
}