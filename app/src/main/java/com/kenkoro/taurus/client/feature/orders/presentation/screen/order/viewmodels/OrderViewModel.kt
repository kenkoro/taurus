package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateManager
import com.kenkoro.taurus.client.feature.shared.viewmodels.util.UserStateObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(userStateManager: UserStateManager) :
  ViewModel(),
    UserStateObserver {
    init {
      userStateManager.addNewObserver(this)
    }

    var user by mutableStateOf<User?>(null)
      private set

    override fun updateUserState(user: User) {
      this.user = user
    }
  }