package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginFieldsContent
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components.LoginHelpContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun LoginScreen(
  subject: String,
  password: String,
  networkStatus: Status,
  scope: CoroutineScope,
  onLoginNavigate: () -> Unit,
  onSubjectChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val snackbarHostState = remember { SnackbarHostState() }

  AppTheme {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            modifier = Modifier.padding(bottom = 80.dp),
            snackbarData = it,
          )
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .padding(it),
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          LoginFieldsContent(
            modifier =
              Modifier
                .width(contentWidth.standard)
                .weight(9F),
            onLoginNavigate = onLoginNavigate,
            snackbarHostState = snackbarHostState,
            subject = subject,
            onSubjectChange = onSubjectChange,
            password = password,
            onPasswordChange = onPasswordChange,
            onLogin = onLogin,
            onLoginResponseChange = onLoginResponseChange,
            networkStatus = networkStatus,
            scope = scope,
          )
          LoginHelpContent(
            modifier = Modifier.weight(1F),
            snackbarHostState = snackbarHostState,
          )
          Spacer(modifier = Modifier.height(contentHeight.medium))
        }
      }
    }
  }
}