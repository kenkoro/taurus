package com.kenkoro.taurus.client.feature.profile.data.remote.dto

import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUserDto(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
)