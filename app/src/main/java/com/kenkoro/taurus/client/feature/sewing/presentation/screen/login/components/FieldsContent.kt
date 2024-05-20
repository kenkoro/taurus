package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarResult
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FieldsContent(
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
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current
  var showError by rememberSaveable { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = stringResource(id = R.string.login_credentials_label),
      style = MaterialTheme.typography.headlineLarge,
    )
    Spacer(modifier = Modifier.height(contentHeight.large * 2))
    Column(horizontalAlignment = Alignment.End) {
      OutlinedTextField(
        isError = showError,
        value = subject,
        onValueChange = { onSubject(it) },
        shape = RoundedCornerShape(shape.large),
        placeholder = {
          Text(text = stringResource(id = R.string.login_subject))
        },
        keyboardOptions =
          KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
          ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
      )
      Spacer(modifier = Modifier.height(contentHeight.large))
      OutlinedTextField(
        isError = showError,
        value = password,
        onValueChange = { onPassword(it) },
        shape = RoundedCornerShape(shape.large),
        placeholder = {
          Text(text = stringResource(id = R.string.login_password))
        },
        keyboardOptions =
          KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
          ),
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
      )
      Spacer(modifier = Modifier.height(contentHeight.large))
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
                onEncryptAll(it.token)

                withContext(Dispatchers.Main) {
                  onNavigateToOrderScreen()
                }
              }

              result.onFailure {
                showError = true
                onLoginErrorShowSnackbar()
              }
            } else {
              showError = true
              onInvalidLoginCredentialsShowSnackbar()
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