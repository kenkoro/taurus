package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.TokenDto
import com.kenkoro.taurus.client.feature.sewing.presentation.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FieldsContent(
  subject: String,
  password: String,
  onSubject: (String) -> Unit,
  onPassword: (String) -> Unit,
  onLogin: suspend () -> Result<TokenDto>,
  onLoginResult: (LoginResult) -> Unit,
  onNavigateToOrderScreen: () -> Unit,
  onEncryptAll: (String) -> Unit,
  errorSnackbarHostState: SnackbarHostState,
  networkStatus: NetworkStatus,
  modifier: Modifier = Modifier,
) {
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  var showError by rememberSaveable { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  val okActionLabel = stringResource(id = R.string.ok)
  val requestErrorMessage = stringResource(id = R.string.request_error)
  val subjectAndPasswordCannotBeBlankMessage =
    stringResource(id = R.string.subject_and_password_cannot_be_blank)

  val loginFields =
    listOf(
      FieldData(
        value = subject,
        onValueChange = {
          onSubject(it)
        },
        placeholderText = stringResource(id = R.string.login_subject),
      ),
      FieldData(
        value = password,
        onValueChange = {
          onPassword(it)
        },
        placeholderText = stringResource(id = R.string.login_password),
        keyboardOptions =
        KeyboardOptions.Default.copy(
          imeAction = ImeAction.Done,
          keyboardType = KeyboardType.Password,
        ),
        transformation = PasswordVisualTransformation(),
      ),
    )
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = stringResource(id = R.string.login_credentials_label),
      style = MaterialTheme.typography.headlineLarge,
    )
    Spacer(modifier = Modifier.height(contentHeight.large * 2))
    LazyColumn(
      horizontalAlignment = Alignment.End,
    ) {
      items(loginFields) { fieldData: FieldData ->
        OutlinedTextField(
          isError = showError,
          value = fieldData.value,
          onValueChange = {
            if (showError) {
              showError = false
            }
            fieldData.onValueChange(it)
          },
          shape = RoundedCornerShape(shape.large),
          placeholder = {
            Text(text = fieldData.placeholderText)
          },
          keyboardOptions = fieldData.keyboardOptions,
          keyboardActions = fieldData.keyboardActions,
          visualTransformation = fieldData.transformation,
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
        )
        Spacer(modifier = Modifier.height(contentHeight.large))
      }
    }
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Button(
        enabled = networkStatus == NetworkStatus.Available,
        modifier =
        Modifier
          .size(width = contentWidth.halfStandard, height = contentHeight.standard),
        shape = RoundedCornerShape(shape.medium),
        onClick = {
          scope.launch(Dispatchers.IO) {
            if (subject.isNotBlank() && password.isNotBlank()) {
              val result = onLogin()
              result.onSuccess {
                onLoginResult(LoginResult.Success)
                onEncryptAll(it.token)
                onNavigateToOrderScreen()
              }

              result.onFailure {
                showError = true
                errorSnackbarHostState.showSnackbar(
                  message = requestErrorMessage,
                  actionLabel = okActionLabel,
                )
              }
            } else {
              showError = true
              errorSnackbarHostState.showSnackbar(
                message = subjectAndPasswordCannotBeBlankMessage,
                actionLabel = okActionLabel,
              )
            }
          }
        },
      ) {
        Row {
          Text(text = stringResource(id = R.string.continue_button))
          Spacer(modifier = Modifier.width(contentWidth.small))
          Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "LoginButton")
        }
      }
    }
  }
}