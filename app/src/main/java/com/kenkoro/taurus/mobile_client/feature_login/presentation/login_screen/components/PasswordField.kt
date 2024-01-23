package com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

object PasswordFieldLiterals {
  const val MAX_LINES = 1
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
  password: MutableState<String>,
  modifier: Modifier = Modifier
) {
  TextField(
    value = password.value,
    onValueChange = { enteredPassword ->
      password.value = enteredPassword
    },
    label = {
      Text(text = "Password")
    },
    leadingIcon = {
      Icon(imageVector = Icons.Default.Lock, contentDescription = null)
    },
    maxLines = PasswordFieldLiterals.MAX_LINES,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = ImeAction.Done
    ),
    modifier = modifier.fillMaxWidth()
  )
}