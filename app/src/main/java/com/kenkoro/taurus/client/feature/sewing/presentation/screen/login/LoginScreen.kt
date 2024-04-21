package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginFieldsContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginHelpContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun LoginScreen(
  subject: String,
  password: String,
  networkStatus: NetworkStatus,
  onLoginNavigate: () -> Unit,
  onSubjectChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  val snackbarHostState = remember { SnackbarHostState() }
  val errorSnackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      snackbarHost = {
        TaurusSnackbar(
          snackbarHostState = snackbarHostState,
          onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
        )

        TaurusSnackbar(
          snackbarHostState = errorSnackbarHostState,
          onDismiss = { errorSnackbarHostState.currentSnackbarData?.dismiss() },
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .padding(it),
        content = {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
          ) {
            LoginFieldsContent(
              modifier =
                Modifier
                  .width(contentWidth.standard)
                  .weight(9F),
              onLoginNavigate = onLoginNavigate,
              subject = subject,
              onSubjectChange = onSubjectChange,
              password = password,
              onPasswordChange = onPasswordChange,
              onLogin = onLogin,
              onLoginResponseChange = onLoginResponseChange,
              networkStatus = networkStatus,
              errorSnackbarHostState = errorSnackbarHostState,
            )
            LoginHelpContent(
              modifier =
                Modifier
                  .weight(1F)
                  .height(contentHeight.standard),
              snackbarHostState = snackbarHostState,
            )
            Spacer(modifier = Modifier.height(contentHeight.medium))
          }
        },
      )
    }
  }
}

@Preview
@Composable
private fun LoginScreenPrev() {
  AppTheme {
    LoginScreen(
      subject = "",
      password = "",
      networkStatus = NetworkStatus.Available,
      onLoginNavigate = { /*TODO*/ },
      onSubjectChange = { _ -> },
      onPasswordChange = { _ -> },
      onLogin = { _, _, _ -> LoginResponse.Success },
      onLoginResponseChange = { _ -> },
    )
  }
}