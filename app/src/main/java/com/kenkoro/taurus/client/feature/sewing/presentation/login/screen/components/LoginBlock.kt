package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponseType
import kotlinx.coroutines.launch

@Composable
fun LoginBlock(
  snackbarHostState: SnackbarHostState,
  loginViewModel: LoginViewModel = hiltViewModel(),
  onLoginNavigate: () -> Unit,
  modifier: Modifier,
) {
  val subject = loginViewModel.subject
  val password = loginViewModel.password
  val context = LocalContext.current
  val loginViewModelScope = loginViewModel.viewModelScope

  val requestErrorMessage = stringResource(id = R.string.request_error)
  val subjectAndPasswordCannotBeBlankMessage =
    stringResource(id = R.string.subject_and_password_cannot_be_blank)

  val loginFields =
    listOf(
      FieldData(
        value = subject.value,
        onValueChange = {
          loginViewModel.subject(it)
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
        value = password.value,
        onValueChange = {
          loginViewModel.password(it)
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
    Spacer(modifier = Modifier.height(30.dp))
    LazyColumn(
      horizontalAlignment = Alignment.End,
    ) {
      items(loginFields) { fieldData: FieldData ->
        OutlinedTextField(
          value = fieldData.value,
          onValueChange = {
            fieldData.onValueChange(it)
          },
          shape = RoundedCornerShape(50),
          placeholder = {
            Text(text = fieldData.placeholderText)
          },
          keyboardOptions = fieldData.keyboardOptions,
          keyboardActions = fieldData.keyboardActions,
          visualTransformation = fieldData.transformation,
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
        )
        Spacer(modifier = Modifier.height(15.dp))
      }
    }
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Button(
        modifier =
          Modifier
            .size(width = 160.dp, height = 80.dp),
        shape = RoundedCornerShape(30.dp),
        onClick = {
          loginViewModelScope.launch {
            val response =
              if (subject.value.isNotBlank() && password.value.isNotBlank()) {
                loginViewModel.loginAndEncryptCredentials(
                  request =
                    LoginRequest(
                      subject = subject.value,
                      password = password.value,
                    ),
                  context = context,
                  encryptSubjectAndPassword = true,
                )
              } else {
                LoginResponseType.BadCredentials
              }

            if (response != LoginResponseType.Success) {
              val message =
                if (response == LoginResponseType.BadCredentials) {
                  subjectAndPasswordCannotBeBlankMessage
                } else {
                  requestErrorMessage
                }

              snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
              )
            } else {
              onLoginNavigate()
            }
          }
        },
      ) {
        Row {
          Text(text = stringResource(id = R.string.continue_button))
          Spacer(modifier = Modifier.width(5.dp))
          Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Login button")
        }
      }
    }
  }
}