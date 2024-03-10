package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.CryptoManager
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.api.UserKtorApi
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.UnresolvedAddressException

@JvmInline
value class ResponseMessage(val value: String) {
  fun isNotSuccess(): Boolean = value.isNotBlank()
}

@Composable
fun LoginBlock(
  onLoginNavigate: () -> Unit = {},
  snackbarHostState: SnackbarHostState,
  viewModel: LoginViewModel = hiltViewModel(),
  @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()
  val subject = viewModel.subject
  val password = viewModel.password
  val context = LocalContext.current

  val loginFields =
    listOf(
      FieldData(
        state = subject,
        placeholderText = stringResource(id = R.string.login_subject),
        keyboardOptions =
          KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
          ),
        transformation = VisualTransformation.None,
      ),
      FieldData(
        state = password,
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
          value = fieldData.state.value,
          onValueChange = { fieldData.state.value = it },
          shape = RoundedCornerShape(50),
          placeholder = {
            Text(text = fieldData.placeholderText)
          },
          keyboardOptions = fieldData.keyboardOptions,
          keyboardActions = fieldData.keyboardActions,
          visualTransformation = fieldData.transformation,
          modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(15.dp))
      }
    }
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.fillMaxWidth(),
    ) {
      val failedInternetConnectionMessage =
        stringResource(id = R.string.check_internet_connection)
      val httpRequestTimeoutExceptionMessage = stringResource(id = R.string.login_timeout)
      val requestErrorMessage = stringResource(id = R.string.request_error)
      Button(
        modifier =
          Modifier
            .size(width = 160.dp, height = 80.dp),
        shape = RoundedCornerShape(30.dp),
        onClick = {
          scope.launch {
            val responseMessage =
              try {
                val response =
                  viewModel.login(
                    LoginRequest(
                      subject = subject.value,
                      password = password.value,
                    ),
                  )

                if (!response.status.isSuccess()) {
                  ResponseMessage(response.status.toString())
                } else {
                  encryptCredentials(
                    credentials = "${subject.value} ${password.value}",
                    context = context,
                  )
                  onLoginNavigate()
                  ResponseMessage("")
                }
              } catch (_: HttpRequestTimeoutException) {
                ResponseMessage(httpRequestTimeoutExceptionMessage)
              } catch (_: UnresolvedAddressException) {
                ResponseMessage(failedInternetConnectionMessage)
              } catch (_: Exception) {
                ResponseMessage(requestErrorMessage)
              }

            if (responseMessage.isNotSuccess()) {
              snackbarHostState.showSnackbar(
                message = responseMessage.value,
                withDismissAction = true,
              )
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

private fun encryptCredentials(
  credentials: String,
  context: Context,
) {
  val bytes = credentials.encodeToByteArray()
  val cryptoManager = CryptoManager()
  val file = File(context.filesDir, "${LocalCredentials.FILENAME}.txt")
  if (!file.exists()) {
    file.createNewFile()
  }
  val fos = FileOutputStream(file)
  cryptoManager.encrypt(
    bytes = bytes,
    outputStream = fos,
  )
}

@Preview(showBackground = true)
@Composable
fun LoginBlockPreview() {
  val snackbarHostState = remember { SnackbarHostState() }
  LoginBlock(
    viewModel =
      LoginViewModel(
        userRepository =
          UserRepositoryImpl(
            UserKtorApi(
              HttpClient(),
            ),
          ),
      ),
    snackbarHostState = snackbarHostState,
  )
}