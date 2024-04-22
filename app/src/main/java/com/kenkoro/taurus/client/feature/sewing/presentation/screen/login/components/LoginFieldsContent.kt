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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.NetworkStatus
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import kotlinx.coroutines.launch

@Composable
fun LoginFieldsContent(
  subject: String,
  password: String,
  modifier: Modifier,
  networkStatus: NetworkStatus,
  errorSnackbarHostState: SnackbarHostState,
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
  var isError by rememberSaveable { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  val requestErrorMessage = stringResource(id = R.string.request_error)
  val subjectAndPasswordCannotBeBlankMessage =
    stringResource(id = R.string.subject_and_password_cannot_be_blank)
  val okActionLabel = stringResource(id = R.string.ok)

  val loginFields =
    listOf(
      FieldData(
        value = subject,
        onValueChange = {
          onSubjectChange(it)
        },
        placeholderText = stringResource(id = R.string.login_subject),
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
          isError = isError,
          value = fieldData.value,
          onValueChange = {
            if (isError) {
              isError = false
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
          modifier =
            Modifier
              .fillMaxWidth(),
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
          scope.launch {
            val response =
              if (subject.isNotBlank() && password.isNotBlank()) {
                val request = LoginRequestDto(subject, password)
                onLogin(request, context, true)
              } else {
                isError = true
                LoginResponse.BadCredentials
              }

            if (response != LoginResponse.Success) {
              isError = true
              val message =
                if (response == LoginResponse.BadCredentials) {
                  subjectAndPasswordCannotBeBlankMessage
                } else {
                  requestErrorMessage
                }

              errorSnackbarHostState.showSnackbar(
                message = message,
                actionLabel = okActionLabel,
              )
            } else {
              isError = false
              onLoginResponseChange(response)
              onLoginNavigate()
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