package com.kenkoro.taurus.client.feature.sewing.domain.model

import com.kenkoro.taurus.client.feature.profile.domain.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class User(
  val userId: Int,
  val subject: String,
  val password: String,
  val image: String,
  val firstName: String,
  val lastName: String,
  val email: String,
  val profile: UserProfile,
  val salt: String,
)