package com.kenkoro.taurus.client.feature.login.presentation.components

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Rule
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.androidTest.E2ETestTags
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.login.presentation.util.PasswordState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

@Composable
fun Password(
  modifier: Modifier = Modifier,
  passwordState: TaurusTextFieldState = remember { PasswordState() },
  imeAction: ImeAction = ImeAction.Done,
  onImeAction: () -> Unit = {},
) {
  val contentWidth = LocalContentWidth.current
  val shape = LocalShape.current
  val view = LocalView.current
  val vibrationEffect =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      VibrationEffect.EFFECT_CLICK
    } else {
      null
    }
  val onClearPassword = {
    passwordState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }
  var isHidden by remember {
    mutableStateOf(true)
  }

  val passwordErrorMessage = stringResource(id = R.string.password_error)
  val emptyTextFieldErrorMessage = stringResource(id = R.string.empty_text_field_error)
  passwordState.setErrorMessage(passwordErrorMessage)

  OutlinedTextField(
    modifier =
      modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          passwordState.onFocusChange(focusState.isFocused)
          if (!passwordState.isFocused) {
            passwordState.enableShowErrors()
          }
        }
        .testTag(E2ETestTags.PASSWORD_TEXT_FIELD),
    value = passwordState.text,
    onValueChange = {
      if (it.length <= 20) {
        passwordState.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.AutoMirrored.Outlined.Rule,
        contentDescription = "PasswordLeadingIcon",
        isError = passwordState.showErrors(),
      )
    },
    trailingIcon = {
      Row {
        IconButton(onClick = { isHidden = !isHidden }) {
          val imageVector =
            if (isHidden) {
              Icons.Default.KeyOff
            } else {
              Icons.Default.Key
            }
          Icon(
            imageVector = imageVector,
            contentDescription = "ShowPasswordIcon",
          )
        }
        if (passwordState.text.isNotEmpty()) {
          IconButton(onClick = onClearPassword) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "ClearPasswordIcon",
            )
          }
        }
        Spacer(modifier = Modifier.width(contentWidth.small))
      }
    },
    placeholder = {
      Text(text = stringResource(id = R.string.login_password))
    },
    textStyle = MaterialTheme.typography.bodyMedium,
    isError = passwordState.showErrors(),
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Password,
      ),
    keyboardActions =
      KeyboardActions(
        onAny = { onImeAction() },
      ),
    supportingText = {
      val errorMessage = passwordState.getError()
      if (errorMessage == null) {
        if (passwordState.text.length == 20) {
          Text(text = stringResource(id = R.string.max_20_supporting_text))
        }
      } else {
        if (passwordState.isBlank()) {
          Text(text = emptyTextFieldErrorMessage)
        } else {
          Text(text = errorMessage)
        }
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
    visualTransformation =
      if (isHidden) {
        PasswordVisualTransformation()
      } else {
        VisualTransformation.None
      },
  )
}