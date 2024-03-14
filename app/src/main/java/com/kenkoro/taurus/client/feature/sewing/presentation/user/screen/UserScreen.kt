package com.kenkoro.taurus.client.feature.sewing.presentation.user.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response.GetUserResponse
import com.kenkoro.taurus.client.feature.sewing.data.util.UserRole
import com.kenkoro.taurus.client.feature.sewing.presentation.user.screen.components.UserScreenButtons
import com.kenkoro.taurus.client.feature.sewing.presentation.util.DecryptedCredentials
import com.kenkoro.taurus.client.feature.sewing.presentation.util.LocalCredentials
import com.kenkoro.taurus.client.ui.theme.AppTheme
import io.ktor.client.call.body
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
  userViewModel: UserViewModel = hiltViewModel(),
  enabled: Boolean = true,
) {
  val context = LocalContext.current
  val firstName =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.SUBJECT_FILENAME,
      context = context,
    ).value
  val token =
    DecryptedCredentials.getDecryptedCredential(
      filename = LocalCredentials.TOKEN_FILENAME,
      context = context,
    ).value
  LaunchedEffect(Unit) {
    userViewModel.viewModelScope.launch {
      val fetchedUser =
        try {
          userViewModel.getUser(firstName, token).body<GetUserResponse>()
        } catch (_: Exception) {
          GetUserResponse(
            id = -1,
            subject = "",
            password = "",
            image = "",
            firstName = "None",
            lastName = "",
            role = UserRole.Others,
            salt = "",
          )
        }
      Log.d("kenkoro", fetchedUser.toString())
      userViewModel.firstName(fetchedUser.firstName)
    }
  }

  AppTheme {
    Surface(
      modifier =
        Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background),
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(10.dp))
          Text(text = userViewModel.firstName.value, style = MaterialTheme.typography.headlineLarge)
          Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Arrow right")
        }
        Spacer(modifier = Modifier.height(40.dp))
        UserScreenButtons { item ->
          Spacer(modifier = Modifier.width(10.dp))
          Button(
            enabled = enabled,
            modifier =
              Modifier
                .size(width = 175.dp, height = 90.dp)
                .shadow(
                  elevation = 4.dp,
                  shape = RoundedCornerShape(30.dp),
                ),
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(30.dp),
          ) {
            Text(text = item.title, textAlign = TextAlign.Center)
          }
          Spacer(modifier = Modifier.width(10.dp))
        }
      }
    }
  }
}