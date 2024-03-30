package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response

import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserResponseDto(
  val id: Int,
  var subject: String,
  var password: String,
  var image: String,
  @SerialName("first_name") var firstName: String,
  @SerialName("last_name") var lastName: String,
  val email: String,
  var profile: UserProfile,
  val salt: String,
)