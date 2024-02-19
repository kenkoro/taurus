package com.kenkoro.taurus.client.feature.login.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
  @PrimaryKey val id: Int,
)