package com.kenkoro.taurus.client.feature.sewing.domain.model

import com.kenkoro.taurus.client.feature.sewing.domain.model.enums.UserProfile

data class NewUser(
  val subject: String,
  val password: String,
  val image: String,
  val firstName: String,
  val lastName: String,
  val email: String,
  val profile: UserProfile,
  val salt: String,
)