package com.kenkoro.taurus.client.feature.shared.data

import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.AuthRepositoryImpl
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import javax.inject.Inject

class SharedViewModelUtils
  @Inject
  constructor(
    private val repository: AuthRepositoryImpl,
  ) : ViewModelUtils {
    override suspend fun auth(
      subject: String,
      password: String,
    ): Result<TokenDto> {
      val result =
        repository.logIn(
          AuthDto(
            subject = subject,
            password = password,
          ),
        )

      return result
    }
  }