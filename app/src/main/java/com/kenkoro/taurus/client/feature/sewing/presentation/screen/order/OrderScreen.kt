package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.core.connectivity.Status
import com.kenkoro.taurus.client.core.local.LocalContentHeight
import com.kenkoro.taurus.client.core.local.LocalContentWidth
import com.kenkoro.taurus.client.core.local.LocalShape
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.LoginRequestDto
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponseDto
import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import com.kenkoro.taurus.client.feature.sewing.domain.model.Order
import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import com.kenkoro.taurus.client.feature.sewing.presentation.screen.order.components.OrderContent
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.ErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.components.showErrorSnackbar
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.handleLoginWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.shared.handlers.handleUserGetWithLocallyScopedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LoginResponse
import com.kenkoro.taurus.client.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrderScreen(
  orders: LazyPagingItems<Order>,
  user: User?,
  networkStatus: Status,
  loginResponse: LoginResponse,
  isLoginFailed: Boolean,
  onLogin: suspend (LoginRequestDto, Context, encryptSubjectAndPassword: Boolean) -> LoginResponse,
  onGetUser: suspend (String, String) -> GetUserResponseDto,
  onGetUserResponseChange: (GetUserResponseDto) -> Unit,
  onLoginResponseChange: (LoginResponse) -> Unit,
) {
  val context = LocalContext.current
  val shape = LocalShape.current
  val contentHeight = LocalContentHeight.current
  val contentWidth = LocalContentWidth.current

  val selectCustomerText = stringResource(id = R.string.select_a_customer)
  val snackbarHostState = remember { SnackbarHostState() }
  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  var customer by rememberSaveable {
    mutableStateOf(selectCustomerText)
  }

  AppTheme {
    Scaffold(
      modifier =
        Modifier
          .statusBarsPadding()
          .navigationBarsPadding(),
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) {
          ErrorSnackbar(
            snackbarData = it,
          )
        }
      },
      topBar = {
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
                contentDescription = "Select a customer",
              )
            }
            DropdownMenu(
              modifier =
                Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(shape.medium)),
              expanded = expanded,
              onDismissRequest = { expanded = !expanded },
            ) {
              DropdownMenuItem(
                text = { Text(text = "Suborbia") },
                onClick = {
                  customer = "Suborbia"
                  expanded = !expanded
                },
              )
              DropdownMenuItem(
                text = { Text(text = "Что-то другое") },
                onClick = {
                  customer = "Что-то другое"
                  expanded = !expanded
                },
              )
            }
            Spacer(modifier = Modifier.height(contentHeight.medium))
          }
        }
        Spacer(modifier = Modifier.height(contentHeight.medium))
      },
      bottomBar = {
        Column(
          modifier =
            Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.background),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
        ) {
          Spacer(modifier = Modifier.height(contentHeight.medium))
          Button(
            enabled = networkStatus == Status.Available && !isLoginFailed,
            modifier =
              Modifier
                .size(contentWidth.standard + 30.dp, contentHeight.halfStandard),
            shape = RoundedCornerShape(shape.small),
            onClick = { /*TODO*/ },
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new order")
          }
          Spacer(modifier = Modifier.height(contentHeight.medium))
        }
      },
    ) {
      Surface(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(it),
      ) {
        if (networkStatus != Status.Available) {
          onLoginResponseChange(LoginResponse.RequestFailure)
          showErrorSnackbar(
            snackbarHostState = snackbarHostState,
            key = networkStatus,
            message = stringResource(id = R.string.check_internet_connection),
            actionLabel = null,
          )
        } else {
          LaunchedEffect(Unit) {
            onLoginResponseChange(LoginResponse.Pending)
            withContext(Dispatchers.IO) {
              launch {
                if (loginResponse != LoginResponse.Success) {
                  handleLoginWithLocallyScopedCredentials(
                    login = { subject, password, encryptThese ->
                      val request =
                        LoginRequestDto(
                          subject = subject,
                          password = password,
                        )
                      onLogin(request, context, encryptThese)
                    },
                    context = context,
                  ).run {
                    onLoginResponseChange(this)
                  }
                }

                handleUserGetWithLocallyScopedCredentials(context = context) { subject, token ->
                  try {
                    onGetUser(subject, token).run {
                      onGetUserResponseChange(this)
                    }
                  } catch (e: Exception) {
                    Log.d("kenkoro", e.message!!)
                  }
                }
              }
            }
          }
        }

        OrderContent(
          orders = orders,
          snackbarHostState = snackbarHostState,
          user = user,
          networkStatus = networkStatus,
          isLoginFailed = isLoginFailed,
        )
      }
    }
  }
}

@Preview
@Composable
private fun OrderScreenPrev() {
  val orders = flow<PagingData<Order>> { }.collectAsLazyPagingItems()
  AppTheme {
    OrderScreen(
      orders = orders,
      user = null,
      networkStatus = Status.Available,
      loginResponse = LoginResponse.Success,
      onLogin = { _, _, _ -> LoginResponse.Success },
      onGetUser = { _, _ ->
        GetUserResponseDto(
          id = -1,
          subject = "",
          password = "",
          image = "",
          firstName = "",
          lastName = "",
          email = "",
          profile = UserProfile.Admin,
          salt = "",
        )
      },
      onGetUserResponseChange = {},
      onLoginResponseChange = {},
      isLoginFailed = false,
    )
  }
}