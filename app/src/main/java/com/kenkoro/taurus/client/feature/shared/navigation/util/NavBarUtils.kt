package com.kenkoro.taurus.client.feature.shared.navigation.util

import androidx.navigation.NavController
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import com.kenkoro.taurus.client.feature.shared.components.util.NavItemWithNavigation
import com.kenkoro.taurus.client.feature.shared.navigation.Screen

class NavBarUtils(private val navController: NavController) {
  private fun navigateTo(route: String) {
    navController.navigate(route) {
      popUpTo(navController.graph.startDestinationId) {
        saveState = true
      }
      restoreState = true
    }
  }

  private fun canAddNewOrders(profile: UserProfile): Boolean = profile == UserProfile.Customer

  private fun canSeeDictionaries(profile: UserProfile): Boolean {
    return profile == UserProfile.Ceo ||
      profile == UserProfile.Manager ||
      profile == UserProfile.Customer
  }

  private fun dynamicNavItems(
    user: User,
    extraAction: () -> Unit,
  ): List<NavItemWithNavigation> {
    val profile = user.profile
    val navItems =
      mutableListOf(
        NavItemWithNavigation(
          details = BottomNavItem.OrderItem,
          navigateToSelectedRoute = { navigateTo(Screen.OrderScreen.route) },
        ),
        NavItemWithNavigation(
          details = BottomNavItem.SearchOrdersItem,
          navigateToSelectedRoute = { navigateTo(Screen.SearchOrdersScreen.route) },
        ),
      )

    if (canAddNewOrders(profile)) {
      navItems +=
        NavItemWithNavigation(
          details = BottomNavItem.OrderEditorItem,
          navigateToSelectedRoute = {
            extraAction()
            navigateTo(Screen.OrderEditorScreen.route)
          },
        )
    }

    if (canSeeDictionaries(profile)) {
      navItems +=
        NavItemWithNavigation(
          details = BottomNavItem.DictionariesItem,
          navigateToSelectedRoute = { navigateTo(Screen.DictionariesScreen.route) },
        )
    }

    navItems +=
      NavItemWithNavigation(
        details = BottomNavItem.ProfileItem,
        navigateToSelectedRoute = { navigateTo(Screen.ProfileScreen.route) },
      )

    return navItems
  }

  fun listOfNavItems(
    user: User?,
    extraAction: () -> Unit,
  ): List<NavItemWithNavigation> {
    return if (user != null) dynamicNavItems(user, extraAction) else emptyList()
  }
}