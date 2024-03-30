package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.feature.sewing.domain.model.User

@Composable
fun UserScreen(
  user: User?,
  networkStatus: Status,
) {
  if (user != null) {
    Text(text = user.toString())
  }
}