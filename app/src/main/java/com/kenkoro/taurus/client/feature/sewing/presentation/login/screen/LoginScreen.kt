package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components.LoginBlock
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  onLogin: () -> Unit,
  viewModel: LoginViewModel = hiltViewModel(),
) {
  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        LoginBlock(
          onLogin = onLogin,
          modifier =
          Modifier
            .width(320.dp)
            .weight(9F),
        )
        Column(
          modifier =
          Modifier
            .wrapContentHeight()
            .weight(1F)
            .clickable {},
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Spacer(modifier = Modifier.height(5.dp))
          Text(
            text = stringResource(id = R.string.login_forgot_password_top),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth(),
          )
          Text(
            text = stringResource(id = R.string.login_forgot_password_bottom),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth(),
          )
          Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  AppTheme {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      LoginScreen(onLogin = {})
    }
  }
}