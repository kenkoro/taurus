package com.kenkoro.taurus.client.feature.shared.viewmodels.util

import com.kenkoro.taurus.client.feature.profile.domain.User

interface UserStateObserver {
  fun updateUserState(user: User)
}