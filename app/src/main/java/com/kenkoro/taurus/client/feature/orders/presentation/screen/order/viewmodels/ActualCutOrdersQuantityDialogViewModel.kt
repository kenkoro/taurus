package com.kenkoro.taurus.client.feature.orders.presentation.screen.order.viewmodels

import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.orders.data.mappers.toNewCutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActualCutOrdersQuantityDialogViewModel
  @Inject
  constructor(
    private val cutOrderRepository: CutOrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    suspend fun addNewCutOrder(cutOrder: NewCutOrder): Result<CutOrderDto> {
      return cutOrderRepository.addNewCutOrder(
        dto = cutOrder.toNewCutOrderDto(),
        token = decryptedCredentialService.storedToken(),
      )
    }
  }