package com.kenkoro.taurus.client.feature.shared.data

import androidx.room.withTransaction
import com.kenkoro.taurus.client.core.crypto.DecryptedCredentialService
import com.kenkoro.taurus.client.feature.auth.data.remote.dto.AuthDto
import com.kenkoro.taurus.client.feature.auth.data.remote.repository.AuthRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.data.mappers.toNewCutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.mappers.toOrderEntity
import com.kenkoro.taurus.client.feature.orders.data.remote.dto.CutOrderDto
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.CutOrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.data.remote.repository.OrderRepositoryImpl
import com.kenkoro.taurus.client.feature.orders.domain.EditOrder
import com.kenkoro.taurus.client.feature.orders.domain.NewCutOrder
import com.kenkoro.taurus.client.feature.shared.data.local.LocalDatabase
import com.kenkoro.taurus.client.feature.shared.data.remote.dto.TokenDto
import javax.inject.Inject

class SharedViewModelUtils
  @Inject
  constructor(
    private val authRepository: AuthRepositoryImpl,
    private val orderRepository: OrderRepositoryImpl,
    private val cutOrderRepository: CutOrderRepositoryImpl,
    private val localDb: LocalDatabase,
    private val decryptedCredentialService: DecryptedCredentialService,
  ) : ViewModelUtils {
    override suspend fun auth(
      subject: String,
      password: String,
    ): Result<TokenDto> {
      val result =
        authRepository.logIn(
          AuthDto(
            subject = subject,
            password = password,
          ),
        )

      return result
    }

    override suspend fun editOrder(
      dto: EditOrder,
      editor: String,
      postAction: () -> Unit,
    ): Boolean {
      val result =
        orderRepository.editOrder(
          dto.toEditOrderDto(),
          editor,
          decryptedCredentialService.storedToken(),
        )
      result.onSuccess {
        localDb.withTransaction { localDb.orderDao.upsert(dto.toOrderEntity(dto.orderId)) }
        postAction()
      }

      return result.isFailure
    }

    override suspend fun addNewCutOrder(cutOrder: NewCutOrder): Result<CutOrderDto> {
      return cutOrderRepository.addNewCutOrder(
        dto = cutOrder.toNewCutOrderDto(),
        token = decryptedCredentialService.storedToken(),
      )
    }
  }