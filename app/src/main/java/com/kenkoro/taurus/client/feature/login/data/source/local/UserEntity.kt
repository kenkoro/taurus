package com.kenkoro.taurus.client.feature.login.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kenkoro.taurus.client.feature.login.util.UserRole

@Entity
data class UserEntity(
  @PrimaryKey val id: Int,
  val username: String,
  val firstName: String,
  val lastName: String,
  val role: UserRole
)