package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.components.UserScreenButtons
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.components.UserTopBar
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun UserScreen(
  userViewModel: UserViewModel = hiltViewModel(),
  networkStatus: Status,
) {
  userViewModel.onLoad(isUserDataLoading = false)
  AppTheme {
    Surface(
      modifier =
      Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        UserTopBar(
          isLoading = userViewModel.isUserDataLoading,
          firstName = userViewModel.user.firstName,
        )
        Spacer(modifier = Modifier.height(40.dp))
        UserScreenButtons { item ->
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            enabled = networkStatus == Status.Available,
            modifier =
            Modifier
              .size(width = 175.dp, height = 90.dp)
              .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(30.dp),
              ),
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(30.dp),
          ) {
            Text(text = item.title, textAlign = TextAlign.Center)
          }
          Spacer(modifier = Modifier.width(10.dp))
        }
      }
    }
  }
}