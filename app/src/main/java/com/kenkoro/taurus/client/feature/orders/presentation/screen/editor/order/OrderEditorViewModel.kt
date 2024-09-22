package com.kenkoro.taurus.client.feature.orders.presentation.screen.editor.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.mappers.toUser
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.NewOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.profile.domain.User
import com.kenkoro.taurus.client.feature.shared.data.SharedViewModelUtils
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderEditorViewModel
  @Inject
  constructor(
    private val sharedUtils: SharedViewModelUtils,
    private val localDb: LocalDatabase,
    private val orderRepository: OrderRepositoryImpl,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModel() {
    // Maybe I should delegate this to some shared place?
    var user by mutableStateOf<User?>(null)
      private set

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

    suspend fun getUserFromLocalDb(subject: String) {
      localDb.withTransaction {
        val userEntity = localDb.userDao.getUser(subject)
        if (userEntity != null) {
          user = userEntity.toUser()
        }
      }
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