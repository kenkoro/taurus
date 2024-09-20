package com.kenkoro.taurus.client.feature.shared.data

import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto

interface ViewModelUtils {
  suspend fun auth(
    subject: String,
    password: String,
  ): Result<TokenDto>

  suspend fun editOrder(
    dto: EditOrder,
    editor: String,
    postAction: () -> Unit,
  ): Boolean

  suspend fun addNewCutOrder(cutOrder: NewCutOrder): Result<CutOrderDto>
}