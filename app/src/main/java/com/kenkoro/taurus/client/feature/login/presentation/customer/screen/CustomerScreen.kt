package com.kenkoro.taurus.client.feature.login.presentation.customer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kenkoro.taurus.client.ui.theme.TaurusTheme

@Composable
fun CustomerScreen() {
  TaurusTheme {
    Scaffold {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(text = "This is the customer screen!")
      }
    }
  }
}