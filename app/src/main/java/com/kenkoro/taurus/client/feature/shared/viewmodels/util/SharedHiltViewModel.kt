package com.kenkoro.taurus.client.feature.shared.viewmodels.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedHiltViewModel(
  navController: NavHostController,
): T {
  val navGraphRoute = destination.parent?.startDestinationRoute ?: return hiltViewModel()
  val parentEntry =
    remember(this) {
      navController.getBackStackEntry(navGraphRoute)
    }

  return hiltViewModel(parentEntry)
}