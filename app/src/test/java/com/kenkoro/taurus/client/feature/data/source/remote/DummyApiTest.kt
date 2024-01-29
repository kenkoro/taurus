package com.kenkoro.taurus.client.feature.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.kenkoro.taurus.client.feature.login.data.repository.DummyApiRepository
import com.kenkoro.taurus.client.feature.login.data.source.remote.DummyApi
import com.kenkoro.taurus.client.feature.login.domain.model.AuthRequest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@JvmInline
value class UserCredentials(val request: AuthRequest)

class DummyApiTest {
  private lateinit var dummyApiRepository: DummyApiRepository

  companion object {
    const val BASE_URL = "https://dummyjson.com"
    private lateinit var userCredentials: List<UserCredentials>

    @JvmStatic
    @BeforeClass
    fun initUserCredentials() {
      userCredentials = mutableListOf()
      userCredentials += UserCredentials(AuthRequest("atuny0", "9uQFF1Lh"))
      userCredentials += UserCredentials(AuthRequest("hbingley1", "CQutx25i8r"))
      userCredentials += UserCredentials(AuthRequest("rshawe2", "OWsTbMUgFc"))
    }
  }

  @Before
  fun setUp() {
    dummyApiRepository = DummyApiRepository(
      configRetrofitClientAndGetApiForTesting()
    )
  }

  private fun configRetrofitClientAndGetApiForTesting(): DummyApi {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .build()

    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create<DummyApi>()
  }

  @Test
  fun `the dummy auth method should be ok`() = runBlocking {
    userCredentials.forEach { credentials ->
      val response = dummyApiRepository.auth(credentials.request)

      assertThat(response.username).isNotEmpty()
      assertThat(response.firstName).isNotEmpty()
      assertThat(response.lastName).isNotEmpty()
    }
  }

  @Test
  fun `the dummy auth method should not be ok`(): Unit = runBlocking {
    try {
      dummyApiRepository.auth(
        AuthRequest("none", "none")
      )
    } catch (he: HttpException) {
      assertThat(he.code()).isEqualTo(400)
    }
  }
}