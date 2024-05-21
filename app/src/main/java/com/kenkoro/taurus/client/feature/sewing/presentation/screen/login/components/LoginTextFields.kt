package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarResult
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
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectStateSaver

@Composable
fun LoginTextFields(
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
  modifier: Modifier = Modifier,
) {
  val subjectErrorMessage = stringResource(id = R.string.subject_error_message)
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

    Subject(
      subjectState = subjectState,
      onImeAction = { focusRequester.requestFocus() },
      modifier = Modifier.focusRequester(focusRequester),
    )
  }
}