package com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.feature.sewing.presentation.dashboard.screen.util.TabItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomBarContent(
  modifier: Modifier = Modifier,
  tabItems: List<TabItem>,
  pagerState: PagerState,
  content: @Composable (index: Int) -> Unit,
) {
  var selectedTabIndex by remember {
    mutableIntStateOf(0)
  }

  LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
    if (!pagerState.isScrollInProgress) {
      selectedTabIndex = pagerState.currentPage
    }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    HorizontalPager(
      state = pagerState,
      modifier =
        Modifier
          .fillMaxWidth()
          .weight(1F),
      userScrollEnabled = false,
    ) { index ->
      content(index)
    }

    TabRow(
      selectedTabIndex = selectedTabIndex,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = MaterialTheme.colorScheme.onBackground,
      divider = {},
      indicator = {},
    ) {
      tabItems.forEachIndexed { index, tabItem ->
        val selected = selectedTabIndex == index
        Tab(
          selected = selected,
          onClick = {
            selectedTabIndex = index
          },
          text = { Text(text = tabItem.title) },
          icon = {
            Icon(
              imageVector =
                if (selected) {
                  tabItem.selectedIcon
                } else {
                  tabItem.unselectedIcon
                },
              contentDescription = tabItem.title,
            )
          },
        )
      }
    }
  }
}