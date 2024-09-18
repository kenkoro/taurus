package com.kenkoro.taurus.client.feature.shared.viewmodels.util

import com.kenkoro.taurus.client.feature.profile.domain.User

class UserStateManager {
  private val observers = mutableListOf<UserStateObserver>()

  fun addNewObserver(observer: UserStateObserver) {
    observers += observer
  }

  fun removeObserver(observer: UserStateObserver) {
    observers -= observer
  }

  fun notifyAll(user: User) {
    observers.forEach { it.updateUserState(user) }
  }
}