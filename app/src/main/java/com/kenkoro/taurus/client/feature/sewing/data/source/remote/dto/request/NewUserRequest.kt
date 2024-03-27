package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request

import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUserRequest(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
)