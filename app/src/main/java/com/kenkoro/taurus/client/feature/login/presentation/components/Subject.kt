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
import androidx.compose.material.icons.automirrored.twotone.Login
import androidx.compose.material.icons.filled.Close
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
import com.kenkoro.taurus.client.feature.login.presentation.util.SubjectState
import com.kenkoro.taurus.client.feature.shared.components.TaurusIcon
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState

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

  val subjectErrorMessage = stringResource(id = R.string.subject_error)
  val emptyTextFieldErrorMessage = stringResource(id = R.string.empty_text_field_error)
  subjectState.setErrorMessage(subjectErrorMessage)

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
        if (subjectState.text.isNotEmpty()) {
          IconButton(onClick = onClearSubject) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "ClearSubjectIcon",
            )
          }
          Spacer(modifier = Modifier.width(contentWidth.small))
        }
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
      val errorMessage = subjectState.getError()
      if (errorMessage == null) {
        if (!subjectState.isFocusedOnce) {
          Text(text = stringResource(id = R.string.subject_supporting_text))
        } else {
          if (subjectState.text.length == 20) {
            Text(text = stringResource(id = R.string.full_subject_supporting_text))
          }
        }
      } else {
        if (subjectState.isBlank()) {
          Text(text = emptyTextFieldErrorMessage)
        } else {
          Text(text = errorMessage)
        }
      }
    },
    shape = RoundedCornerShape(shape.medium),
    singleLine = true,
  )
}