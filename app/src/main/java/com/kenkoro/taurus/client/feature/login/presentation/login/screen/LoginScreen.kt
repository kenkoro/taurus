package com.kenkoro.taurus.client.feature.login.presentation.login.screen

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.login.presentation.login.screen.components.LoginBlock
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  navController: NavController,
  viewModel: LoginViewModel = hiltViewModel()
) {
  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize()
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        LoginBlock(
          onAuth = {},
          modifier = Modifier
            .width(320.dp)
            .weight(9F)
        )
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .clickable {},
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Bottom
        ) {
          Text(
            text = stringResource(id = R.string.login_forgot_password_top),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.wrapContentSize()
          )
          Text(
            text = stringResource(id = R.string.login_forgot_password_bottom),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.wrapContentSize()
          )
          Spacer(modifier = Modifier.height(30.dp))
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  val navController = rememberNavController()
  AppTheme {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      LoginScreen(navController = navController)
    }
  }
}
