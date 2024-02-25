package com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequest
import com.kenkoro.taurus.client.feature.sewing.presentation.login.screen.LoginViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException

@Composable
fun LoginFieldBlock(
  onLogin: () -> Unit,
  snackbarHostState: SnackbarHostState,
  viewModel: LoginViewModel = hiltViewModel(),
  @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()
  val subject = viewModel.subject
  val password = viewModel.password
  val focusManager = LocalFocusManager.current

  Column(
    horizontalAlignment = Alignment.End,
  ) {
    OutlinedTextField(
      value = subject.value,
      onValueChange = { subject.value = it },
      shape = RoundedCornerShape(50),
      placeholder = {
        Text(text = stringResource(id = R.string.login_subject))
      },
      keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = ImeAction.Next,
        keyboardType = KeyboardType.Text,
      ),
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(15.dp))
    OutlinedTextField(
      value = password.value,
      onValueChange = { password.value = it },
      shape = RoundedCornerShape(50),
      placeholder = {
        Text(text = stringResource(id = R.string.login_password))
      },
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
      ),
      keyboardActions =
      KeyboardActions(
        onDone = { focusManager.clearFocus() },
      ),
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(15.dp))
    Button(
      modifier =
      Modifier
        .clip(RoundedCornerShape(30.dp))
        .size(width = 160.dp, height = 80.dp),
      onClick = {
        scope.launch {
          val message = try {
            viewModel.login(
              LoginRequest(
                subject = subject.value,
                password = password.value,
              ),
            ).status
          } catch (te: HttpRequestTimeoutException) {
            HttpStatusCode.RequestTimeout
          } catch (eae: UnresolvedAddressException) {
            "Check the Internet connection"
          }

          snackbarHostState.showSnackbar(
            message = message.toString(),
            withDismissAction = true,
          )
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