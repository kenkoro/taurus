package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.shared.data.SharedViewModelUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditorViewModel
  @Inject
  constructor(
    private val sharedUtils: SharedViewModelUtils,
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    suspend fun addNewCutOrder(cutOrder: NewCutOrder): Result<CutOrderDto> {
      return sharedUtils.addNewCutOrder(cutOrder)
    }

    suspend fun editOrder(
      dto: EditOrder,
      editor: String,
      postAction: () -> Unit = {},
    ): Boolean {
      return sharedUtils.editOrder(dto, editor, postAction)
    }

    suspend fun addNewOrder(dto: NewOrderDto): Boolean {
      val result =
        orderRepository.addNewOrder(
          dto = dto,
          token = decryptedCredentialService.storedToken(),
        )

      result.onSuccess { /* Do something on a success response */ }
      return result.isSuccess
    }
  }