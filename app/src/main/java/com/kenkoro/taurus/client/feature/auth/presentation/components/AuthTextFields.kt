package com.kenkoro.taurus.client.feature.auth.presentation.components

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
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalOffset
import com.kenkoro.taurus.client.feature.auth.presentation.components.util.AuthExtras
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenShared
import com.kenkoro.taurus.client.feature.auth.presentation.util.AuthScreenUtils

@Composable
fun AuthTextFields(
  modifier: Modifier = Modifier,
  shared: AuthScreenShared,
  extras: AuthExtras,
  utils: AuthScreenUtils,
) {
  val offset = LocalOffset.current
  val focusManager = LocalFocusManager.current
  val contentHeight = LocalContentHeight.current

  val focusRequester = remember { FocusRequester() }
  val subject = utils.subject
  val password = utils.password

  val isSubjectInvalidWhilePasswordIsFocused = { !subject.isValid && password.isFocused }
  val isSubjectInvalidWhilePasswordIsNotFocused = { !subject.isValid && !password.isFocused }
  val isPasswordInvalidWhileNoFocus =
    { !password.isValid && !password.isFocused && !subject.isFocused }
  val areNotFocusedAtAllFromTheBeginning = { !subject.isFocusedOnce && !password.isFocusedOnce }
  val onSubmit = {
    if (subject.isValid && password.isValid) {
      focusManager.clearFocus()
      extras.whenLoginSubmitted(subject.text, password.text)
    }

    if (isSubjectInvalidWhilePasswordIsFocused() || isPasswordInvalidWhileNoFocus()) {
      focusManager.moveFocus(FocusDirection.Up)
    }

    if (isSubjectInvalidWhilePasswordIsNotFocused() || areNotFocusedAtAllFromTheBeginning()) {
      focusRequester.requestFocus()
      focusManager.moveFocus(FocusDirection.Up)
    }
  }
  val isLoginButtonEnable = { shared.network == NetworkStatus.Available }

  val yTargetValue = {
    if (subject.isFocused || password.isFocused) {
      offset.standard
    } else {
      offset.none
    }
  }
  val y by animateDpAsState(
    targetValue = yTargetValue(),
    label = "auth text fields composable: y offset",
  )
  val titleColor = @Composable {
    if (utils.showErrorTitle()) {
      MaterialTheme.colorScheme.error
    } else {
      LocalContentColor.current
    }
  }

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
    AuthButton(
      isLoginButtonEnable = isLoginButtonEnable,
      isLogging = extras.isAuthenticating,
      isError = subject.showErrors() || password.showErrors(),
      onSubmit = onSubmit,
      onExit = shared.exit,
    )
  }
}