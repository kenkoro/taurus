package com.kenkoro.taurus.client.feature.sewing.domain.model

import com.kenkoro.taurus.client.feature.sewing.data.util.UserProfile

data class User(
  val id: Int,
  val subject: String,
  val password: String,
  val image: String,
  val firstName: String,
  val lastName: String,
  val email: String,
  val profile: UserProfile,
  val salt: String
)
