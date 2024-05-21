package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectStateSaver

@Composable
fun LoginTextFields(
  modifier: Modifier = Modifier,
  subject: String,
  password: String,
  onSubject: (String) -> Unit,
  onPassword: (String) -> Unit,
  onLogin: suspend () -> Result<TokenDto>,
  onNavigateToOrderScreen: () -> Unit,
  onEncryptAll: (String) -> Unit,
  onLoginErrorShowSnackbar: suspend () -> SnackbarResult,
  onInvalidLoginCredentialsShowSnackbar: suspend () -> SnackbarResult,
  networkStatus: NetworkStatus,
) {
  val contentHeight = LocalContentHeight.current
  val subjectErrorMessage = stringResource(id = R.string.subject_error_message)
  val passwordErrorMessage = stringResource(id = R.string.password_error_message)
  val emptyTextFieldErrorMessage = stringResource(id = R.string.empty_text_field_error_message)

  val focusRequester = remember { FocusRequester() }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val subjectState by rememberSaveable(stateSaver = SubjectStateSaver) {
      mutableStateOf(SubjectState(subject, subjectErrorMessage, emptyTextFieldErrorMessage))
    }
    val passwordState =
      remember { PasswordState(password, passwordErrorMessage, emptyTextFieldErrorMessage) }
    val showErrorTitle = {
      subjectState.showErrors() || passwordState.showErrors()
    }

    Text(
      text = stringResource(id = R.string.login_screen_title),
      style = MaterialTheme.typography.titleLarge,
      color =
        if (showErrorTitle()) {
          MaterialTheme.colorScheme.error
        } else {
          LocalContentColor.current
        },
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    Subject(
      subjectState = subjectState,
      onImeAction = { focusRequester.requestFocus() },
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    Password(
      passwordState = passwordState,
      onImeAction = { focusRequester.requestFocus() },
      modifier = Modifier.focusRequester(focusRequester),
    )
  }
}