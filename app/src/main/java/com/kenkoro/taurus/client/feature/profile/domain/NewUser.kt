package com.kenkoro.taurus.client.feature.profile.domain

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