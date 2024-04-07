package com.kenkoro.taurus.client.feature.sewing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.kenkoro.taurus.client.feature.sewing.data.source.local.OrderEntity
import com.kenkoro.taurus.client.feature.sewing.data.source.mappers.toOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrderViewModel
  @Inject
  constructor(
    pager: Pager<Int, OrderEntity>,
  ) : ViewModel() {
    val orderPagingFlow =
      pager
        .flow
        .map { pagingData ->
          pagingData.map { it.toOrder() }
        }
        .cachedIn(viewModelScope)
  }