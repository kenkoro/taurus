package com.kenkoro.taurus.mobile_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kenkoro.taurus.mobile_client.feature_login.data.repository.TaurusRepository
import com.kenkoro.taurus.mobile_client.feature_login.data.source.TaurusApi
import com.kenkoro.taurus.mobile_client.feature_login.domain.repository.Repository
import com.kenkoro.taurus.mobile_client.feature_login.presentation.login_screen.LoginScreen
import com.kenkoro.taurus.mobile_client.feature_login.presentation.util.ApiLiterals
import com.kenkoro.taurus.mobile_client.ui.theme.TaurusTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val repository = configureApiAndGetRepository()
    setContent {
      TaurusTheme {
        LoginScreen(repository = repository)
      }
    }
  }

  private fun configureApiAndGetRepository(): Repository {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(ApiLiterals.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    val api = retrofit.create<TaurusApi>()

    return TaurusRepository(api)
  }
}