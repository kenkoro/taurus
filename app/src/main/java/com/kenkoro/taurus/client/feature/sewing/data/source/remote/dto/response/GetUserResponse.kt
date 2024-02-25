package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.response

import com.kenkoro.taurus.client.feature.sewing.data.util.UserRole
import kotlinx.serialization.SerialName

data class GetUserResponse(
  val id: Int,
  var subject: String,
  var password: String,
  var image: String,
  @SerialName("first_name") var firstName: String,
  @SerialName("last_name") var lastName: String,
  var role: UserRole,
  val salt: String,
)