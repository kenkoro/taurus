package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth

@Composable
fun OrderTopBar(networkStatus: Status) {
  val contentHeight = LocalContentHeight.current
  val contentWidth = LocalContentWidth.current

  val selectCustomerText = stringResource(id = R.string.all_orders)
  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  val customer by rememberSaveable {
    mutableStateOf(selectCustomerText)
  }

  Column(
    modifier =
      Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Column(
      modifier =
        Modifier
          .clickable(enabled = networkStatus == Status.Available) {
            expanded = !expanded
          },
    ) {
      Spacer(modifier = Modifier.height(contentHeight.medium))
      Row(
        modifier =
          Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = customer)
        Spacer(modifier = Modifier.width(contentWidth.medium))
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "SelectACustomer",
        )
      }
      Spacer(modifier = Modifier.height(contentHeight.medium))
    }
  }
  Spacer(modifier = Modifier.height(contentHeight.medium))
}