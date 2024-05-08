package com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteDto(
  @SerialName("deleter_subject") val deleterSubject: String,
)