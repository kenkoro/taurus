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
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.twotone.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util.SubjectState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.TaurusIcon

@Composable
fun Subject(
  modifier: Modifier = Modifier,
  subjectState: TaurusTextFieldState = remember { SubjectState() },
  imeAction: ImeAction = ImeAction.Next,
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
  val onClearSubject = {
    subjectState.text = ""

    vibrationEffect?.let {
      view.performHapticFeedback(it)
    }
    Unit
  }

  OutlinedTextField(
    modifier =
      Modifier
        .fillMaxWidth()
        .onFocusChanged { focusState ->
          subjectState.onFocusChange(focusState.isFocused)
          if (!subjectState.isFocused) {
            subjectState.enableShowErrors()
          }
        },
    value = subjectState.text,
    onValueChange = {
      if (it.length <= 20) {
        subjectState.text = it
      }
    },
    leadingIcon = {
      TaurusIcon(
        imageVector = Icons.AutoMirrored.TwoTone.Login,
        contentDescription = "SubjectLeadingIcon",
        isError = subjectState.showErrors(),
      )
    },
    trailingIcon = {
      Row {
        IconButton(onClick = onClearSubject) {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.Backspace,
            contentDescription = "SubjectTrailingIcon",
          )
        }
        Spacer(modifier = Modifier.width(contentWidth.small))
      }
    },
    placeholder = {
      Text(text = stringResource(id = R.string.login_subject))
    },
    textStyle = MaterialTheme.typography.bodyMedium,
    isError = subjectState.showErrors(),
    keyboardOptions =
      KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
      ),
    keyboardActions =
      KeyboardActions(
        onAny = { onImeAction() },
      ),
    supportingText = {
      val error = subjectState.getError()
      if (error == null) {
        if (!subjectState.isFocusedOnce) {
          Text(text = stringResource(id = R.string.subject_supporting_text))
        } else {
          if (subjectState.text.length == 20) {
            Text(text = stringResource(id = R.string.full_subject_supporting_text))
          }
        }
      } else {
        Text(text = error)
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}