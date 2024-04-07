package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

import android.content.Context
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginFieldsContent(
  snackbarHostState: SnackbarHostState,
  subject: String,
  password: String,
  networkStatus: Status,
  modifier: Modifier,
  scope: CoroutineScope,
  onSubjectChange: (String) -> Unit,
  onLoginNavigate: () -> Unit,
  onPasswordChange: (String) -> Unit,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val context = LocalContext.current
  val shape = LocalShape.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val requestErrorMessage = stringResource(id = R.string.request_error)
  val subjectAndPasswordCannotBeBlankMessage =
    stringResource(id = R.string.subject_and_password_cannot_be_blank)
  val actionLabelMessage = stringResource(id = R.string.ok)

  val loginFields =
    listOf(
      FieldData(
        value = subject,
        onValueChange = {
          onSubjectChange(it)
        },
        placeholderText = stringResource(id = R.string.login_subject),
        keyboardOptions =
          KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
          ),
        transformation = VisualTransformation.None,
      ),
      FieldData(
        value = password,
        onValueChange = {
          onPasswordChange(it)
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
    if (networkStatus != Status.Available) {
      showErrorSnackbar(
        snackbarHostState = snackbarHostState,
        key = networkStatus,
        message = stringResource(id = R.string.check_internet_connection),
        actionLabel = null,
      )
    }

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
          value = fieldData.value,
          onValueChange = {
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
        enabled = networkStatus == Status.Available,
        modifier =
          Modifier
            .size(width = contentWidth.halfStandard, height = contentHeight.standard),
        shape = RoundedCornerShape(shape.medium),
        onClick = {
          scope.launch {
            val response =
              if (subject.isNotBlank() && password.isNotBlank()) {
                val request = LoginRequestDto(subject, password)
                onLogin(request, context, true)
              } else {
                LoginResponse.BadCredentials
              }

            if (response != LoginResponse.Success) {
              val message =
                if (response == LoginResponse.BadCredentials) {
                  subjectAndPasswordCannotBeBlankMessage
                } else {
                  requestErrorMessage
                }

              snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabelMessage,
                withDismissAction = false,
                duration = SnackbarDuration.Indefinite,
              )
            } else {
              onLoginResponseChange(response)
              onLoginNavigate()
            }
          }
        },
      ) {
        Row {
          Text(text = stringResource(id = R.string.continue_button))
          Spacer(modifier = Modifier.width(contentWidth.small))
          Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Login button")
        }
      }
    }
  }
}