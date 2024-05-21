package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util

import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.textFieldStateSaver
import java.util.regex.Pattern

private const val SUBJECT_VALIDATION_REGEX = "[a-zA-Z0-9]+"

class SubjectState(
  private val subject: String? = null,
  private val errorMessage: String? = null,
  private val emptySubjectErrorMessage: String = "",
) : TaurusTextFieldState(
    validator = ::isSubjectValid,
    errorFor = { text ->
      subjectValidationError(text, errorMessage, emptySubjectErrorMessage)
    },
  ) {
  init {
    subject?.let {
      text = it
    }
  }
}

private fun subjectValidationError(
  subject: String,
  errorMessage: String?,
  emptySubjectErrorMessage: String,
): String {
  return if (errorMessage == null) {
    "$subject is invalid"
  } else {
    if (subject.isBlank()) {
      emptySubjectErrorMessage
    } else {
      "$subject $errorMessage!"
    }
  }
}

private fun isSubjectValid(subject: String): Boolean {
  return Pattern.matches(SUBJECT_VALIDATION_REGEX, subject)
}

val SubjectStateSaver = textFieldStateSaver(SubjectState())