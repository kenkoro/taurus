package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto

import com.kenkoro.taurus.client.feature.sewing.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class UserDto(val user: User)