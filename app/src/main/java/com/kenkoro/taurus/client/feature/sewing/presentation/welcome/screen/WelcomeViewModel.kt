package com.kenkoro.taurus.client.feature.sewing.presentation.welcome.screen

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel
  @Inject
  constructor() : ViewModel() {
    fun performKeyboardPressHapticFeedback(view: View) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS)
      }
    }
  }