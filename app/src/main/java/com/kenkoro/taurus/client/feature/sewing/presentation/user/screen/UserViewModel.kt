package com.kenkoro.taurus.client.feature.sewing.presentation.user.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.sewing.data.source.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

@HiltViewModel
class UserViewModel
  @Inject
  constructor(
    private val userRepository: UserRepositoryImpl,
  ) : ViewModel() {
    private val _firstName = mutableStateOf("")
    val firstName = _firstName

    fun firstName(firstName: String) {
      _firstName.value = firstName
    }

    suspend fun getUser(
      subject: String,
      token: String,
    ): HttpResponse {
      return userRepository.run {
        token(token)
        getUser(subject)
      }
    }
  }