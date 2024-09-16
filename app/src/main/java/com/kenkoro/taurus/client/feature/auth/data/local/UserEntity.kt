package com.kenkoro.taurus.client.feature.auth.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kenkoro.taurus.client.feature.profile.domain.UserProfile

@Entity(
  tableName = "user_entities",
)
data class UserEntity(
  @PrimaryKey(autoGenerate = true) @ColumnInfo("user_id") val userId: Int = 0,
  val subject: String,
  val password: String,
  val image: String,
  @ColumnInfo("first_name") val firstName: String,
  @ColumnInfo("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
  val salt: String,
)