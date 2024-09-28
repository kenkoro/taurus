package com.kenkoro.taurus.client.feature.shared.navigation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.shared.navigation.Screen

sealed class BottomNavItem(
  val route: String,
  val icon: @Composable () -> Unit = {},
  val label: @Composable () -> Unit = {},
) {
  data object OrderItem : BottomNavItem(
    route = Screen.OrderScreen.route,
    icon = {
      Icon(
        imageVector = Icons.Default.Home,
        contentDescription = "bottom bar item sealed class: order item",
      )
    },
    label = { Text(text = stringResource(id = R.string.order_item)) },
  )

  data object SearchOrdersItem : BottomNavItem(
    route = Screen.SearchOrdersScreen.route,
    icon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "bottom bar item sealed class: search orders item",
      )
    },
    label = { Text(text = stringResource(id = R.string.search_orders_item)) },
  )

  data object OrderEditorItem : BottomNavItem(
    route = Screen.OrderEditorScreen.route,
    icon = {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "bottom bar item sealed class: order editor item",
      )
    },
    label = { Text(text = stringResource(id = R.string.order_editor_item)) },
  )

  data object DictionariesItem : BottomNavItem(
    route = Screen.DictionariesScreen.route,
    icon = {
      Icon(
        imageVector = Icons.Default.Bookmarks,
        contentDescription = "bottom bar item sealed class: dictionaries item",
      )
    },
    label = { Text(text = stringResource(id = R.string.dictionaries_item)) },
  )

  data object ProfileItem : BottomNavItem(
    route = Screen.ProfileScreen.route,
    icon = {
      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "bottom bar item sealed class: profile item",
      )
    },
    label = { Text(text = stringResource(id = R.string.profile_item)) },
  )
}