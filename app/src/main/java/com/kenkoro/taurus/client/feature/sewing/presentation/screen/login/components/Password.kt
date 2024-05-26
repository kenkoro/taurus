package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.PasswordState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusIcon

@Composable
fun Password(
  passwordState: TaurusTextFieldState = remember { PasswordState() },
  imeAction: ImeAction = ImeAction.Done,
  onImeAction: () -> Unit = {},
  modifier: Modifier,
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

  OutlinedTextField(
    modifier =
      modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          passwordState.onFocusChange(focusState.isFocused)
          if (!passwordState.isFocused) {
            passwordState.enableShowErrors()
          }
        },
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
      val error = passwordState.getError()
      if (error == null) {
        if (passwordState.text.length == 20) {
          Text(text = stringResource(id = R.string.full_subject_supporting_text))
        }
      } else {
        Text(text = error)
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