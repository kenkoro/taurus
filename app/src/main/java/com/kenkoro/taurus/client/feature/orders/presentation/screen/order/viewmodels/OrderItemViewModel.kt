package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.ActualCutOrdersQuantityDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderItemViewModel
  @Inject
  constructor(
    private val cutOrderRepository: CutOrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    var selectedOrderRecordId by mutableStateOf<Int?>(null)
      private set

    fun newOrderSelection(recordId: Int) {
      selectedOrderRecordId = recordId
    }

    fun clearOrderSelection() {
      selectedOrderRecordId = null
    }

    suspend fun getActualQuantityOfCutMaterial(orderId: Int): Result<ActualCutOrdersQuantityDto> {
      return cutOrderRepository.getActualCutOrdersQuantity(
        orderId = orderId,
        token = decryptedCredentialService.storedToken(),
      )
    }
  }