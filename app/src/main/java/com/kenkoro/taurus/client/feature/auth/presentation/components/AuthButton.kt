package com.kenkoro.taurus.client.feature.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.core.local.LocalSize
import com.kenkoro.taurus.client.core.local.LocalStrokeWidth
import com.kenkoro.taurus.client.ui.theme.AppTheme

@Composable
fun AuthButton(
  modifier: Modifier = Modifier,
  isLogging: Boolean = false,
  isError: Boolean = false,
  isLoginButtonEnable: () -> Boolean = { true },
  onSubmit: () -> Unit = {},
  onExit: () -> Unit = {},
) {
  val size = LocalSize.current
  val shape = LocalShape.current
  val strokeWidth = LocalStrokeWidth.current
  val contentWidth = LocalContentWidth.current
  val contentHeight = LocalContentHeight.current

  val continueButtonColors =
    ButtonDefaults.buttonColors(
      containerColor =
        if (isError) {
          MaterialTheme.colorScheme.error
        } else {
          MaterialTheme.colorScheme.primary
        },
      contentColor =
        if (isError) {
          MaterialTheme.colorScheme.onError
        } else {
          MaterialTheme.colorScheme.onPrimary
        },
    )

  Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Button(
      onClick = { onExit() },
      enabled = isLoginButtonEnable(),
      modifier =
        modifier
          .width(contentWidth.halfStandard)
          .height(contentHeight.loginButton),
      shape = RoundedCornerShape(topStart = shape.medium, bottomStart = shape.medium),
    ) {
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "auth button composable: exit the app",
        )
        Spacer(modifier = Modifier.width(contentWidth.medium))
        Text(text = stringResource(id = R.string.exit_button))
      }
    }
    Divider(
      modifier =
        Modifier
          .height(contentHeight.loginButton)
          .width(1.dp),
    )
    Button(
      onClick = { onSubmit() },
      enabled = isLoginButtonEnable(),
      colors = continueButtonColors,
      modifier =
        modifier
          .width(contentWidth.halfStandard)
          .height(contentHeight.loginButton),
      shape = RoundedCornerShape(topEnd = shape.medium, bottomEnd = shape.medium),
    ) {
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = stringResource(id = R.string.continue_button))
        Spacer(modifier = Modifier.width(contentWidth.medium))
        if (isLogging) {
          CircularProgressIndicator(
            strokeWidth = strokeWidth.small,
            modifier = Modifier.size(size.small),
            color = MaterialTheme.colorScheme.onPrimary,
          )
        } else {
          Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "auth button composable: log in",
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun LoginButtonPrev() {
  AppTheme {
    AuthButton()
  }
}