package com.kenkoro.taurus.client.feature.sewing.presentation.screen.login.util

import com.kenkoro.taurus.client.feature.sewing.presentation.shared.TaurusTextFieldState
import java.util.regex.Pattern

private const val SUBJECT_VALIDATION_REGEX = "[a-zA-Z0-9]+"

class SubjectState(
  subject: String? = null,
) : TaurusTextFieldState(
    validator = ::isSubjectValid,
    errorFor = ::subjectValidationError,
  ) {
  init {
    subject?.let {
      text = it
    }
  }
}

fun subjectValidationError(
  subject: String,
  errorMessage: String,
  emptyTextFieldErrorMessage: String,
): String {
  return if (subject.isBlank()) {
    emptyTextFieldErrorMessage
  } else {
    "$subject $errorMessage!"
  }
}

private fun isSubjectValid(subject: String): Boolean {
  return Pattern.matches(SUBJECT_VALIDATION_REGEX, subject)
}