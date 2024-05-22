package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.LoginButton

@Composable
fun LoginTextFields(
  modifier: Modifier = Modifier,
  networkStatus: NetworkStatus,
  subject: SubjectState,
  password: PasswordState,
  onSetErrorMessages: (TaurusTextFieldState, String, String) -> Unit,
  onLoginSubmitted: (subject: String, password: String) -> Unit,
  onExit: () -> Unit = {},
) {
  val contentHeight = LocalContentHeight.current
  val focusManager = LocalFocusManager.current

  val subjectErrorMessage = stringResource(id = R.string.subject_error_message)
  val passwordErrorMessage = stringResource(id = R.string.password_error_message)
  val emptyTextFieldErrorMessage = stringResource(id = R.string.empty_text_field_error_message)

  val focusRequester = remember { FocusRequester() }

  onSetErrorMessages(subject, subjectErrorMessage, emptyTextFieldErrorMessage)
  onSetErrorMessages(password, passwordErrorMessage, emptyTextFieldErrorMessage)
  val showErrorTitle = {
    subject.showErrors() || password.showErrors()
  }
  val titleColor = @Composable {
    if (showErrorTitle()) {
      MaterialTheme.colorScheme.error
    } else {
      LocalContentColor.current
    }
  }
  val isSubjectInvalidWhilePasswordIsFocused = { !subject.isValid && password.isFocused }
  val isSubjectInvalidWhilePasswordIsNotFocused = { !subject.isValid && !password.isFocused }
  val isPasswordInvalidWhileNoFocus =
    { !password.isValid && !password.isFocused && !subject.isFocused }
  val areNotFocusedAtAllAtTheStart = { !subject.isFocusedOnce && !password.isFocusedOnce }
  val onSubmit = {
    if (subject.isValid && password.isValid) {
      onLoginSubmitted(subject.text, password.text)
      focusManager.clearFocus()
    }

    if (isSubjectInvalidWhilePasswordIsFocused() || isPasswordInvalidWhileNoFocus()) {
      focusManager.moveFocus(FocusDirection.Up)
    }

    if (isSubjectInvalidWhilePasswordIsNotFocused() || areNotFocusedAtAllAtTheStart()) {
      focusRequester.requestFocus()
      focusManager.moveFocus(FocusDirection.Up)
    }
  }
  val isLoginButtonEnable = { networkStatus == NetworkStatus.Available }
  val yTargetValue = {
    if (subject.isFocused || password.isFocused) {
      (-30).dp
    } else {
      50.dp
    }
  }
  val y by animateDpAsState(targetValue = yTargetValue(), label = "")

  Column(
    modifier = modifier.offset(y = y),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = stringResource(id = R.string.login_screen_title),
      style = MaterialTheme.typography.titleLarge,
      color = titleColor(),
    )
    Spacer(modifier = Modifier.height(contentHeight.large))
    Subject(
      subjectState = subject,
      onImeAction = { focusRequester.requestFocus() },
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    Password(
      passwordState = password,
      onImeAction = { onSubmit() },
      modifier = Modifier.focusRequester(focusRequester),
    )
    Spacer(modifier = Modifier.height(contentHeight.medium))
    LoginButton(
      isLoginButtonEnable = isLoginButtonEnable,
      onSubmit = onSubmit,
      onExit = onExit,
      isError = subject.showErrors() || password.showErrors(),
    )
  }
}