package com.kenkoro.taurus.client.feature.sewing.presentation.welcome.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenkoro.taurus.client.R
import com.kenkoro.taurus.client.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WelcomeScreen(
  onContinue: () -> Unit,
  viewModel: WelcomeViewModel = hiltViewModel(),
) {
  val view = LocalView.current
  val font = FontFamily(Font(R.font.just_me_again_down_here))

  AppTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
    ) {
      Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Image(
          painter = painterResource(id = R.drawable.welcome),
          contentDescription = "Welcome picture",
          modifier =
            Modifier
              .fillMaxSize(0.7F)
              .weight(1F),
        )
        Column(
          modifier = Modifier.weight(1F),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Column(
            modifier = Modifier.weight(2F),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(
              text = stringResource(id = R.string.welcome_medium),
              style = MaterialTheme.typography.bodyMedium,
              fontFamily = font,
              fontSize = 24.sp,
              modifier =
                Modifier
                  .wrapContentHeight(),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
              text = stringResource(id = R.string.welcome_large),
              style = MaterialTheme.typography.bodyLarge,
              fontFamily = font,
              fontSize = 48.sp,
              color = MaterialTheme.colorScheme.primary,
              modifier =
                Modifier
                  .wrapContentSize(),
            )
          }
          Column(
            modifier = Modifier.weight(3F),
            verticalArrangement = Arrangement.Bottom,
          ) {
            Text(
              text = stringResource(id = R.string.welcome_warning),
              style = MaterialTheme.typography.bodySmall,
              fontFamily = font,
              fontSize = 16.sp,
              textAlign = TextAlign.Center,
              modifier =
                Modifier
                  .wrapContentHeight()
                  .width(320.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
              modifier =
                Modifier
                  .width(320.dp)
                  .padding(bottom = 15.dp),
              onClick = {
                if (!view.isInEditMode) {
                  viewModel.performKeyboardPressHapticFeedback(view)
                }

                onContinue()
              },
            ) {
              Text(
                text = stringResource(id = R.string.continue_button),
                style = MaterialTheme.typography.labelMedium,
              )
              Spacer(modifier = Modifier.width(10.dp))
              Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Continue button")
            }
            Spacer(Modifier.height(30.dp))
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
  AppTheme {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      WelcomeScreen(onContinue = {})
    }
  }
}