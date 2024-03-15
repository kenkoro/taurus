package com.kenkoro.taurus.client.feature.sewing.presentation.screen.user.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
  isLoading: Boolean,
  firstName: String,
) {
  Spacer(modifier = Modifier.height(10.dp))
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.width(10.dp))
    if (isLoading) {
      CircularProgressIndicator()
    } else {
      Text(
        text = firstName,
        style = MaterialTheme.typography.headlineLarge,
      )
      Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Right arrow")
    }
  }
}