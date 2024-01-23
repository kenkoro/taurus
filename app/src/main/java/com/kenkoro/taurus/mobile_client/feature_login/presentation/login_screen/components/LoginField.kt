package com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

object LoginFieldLiterals {
  const val MAX_LINES = 1
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
  userName: MutableState<String>,
  modifier: Modifier = Modifier
) {
  TextField(
    value = userName.value,
    onValueChange = { newUserName ->
      userName.value = newUserName
    },
    placeholder = {
      Text(text = "Your login")
    },
    leadingIcon = {
      Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
    },
    maxLines = LoginFieldLiterals.MAX_LINES,
    keyboardOptions = KeyboardOptions(
      imeAction = ImeAction.Next
    ),
    modifier = modifier
  )
}