package com.kenkoro.taurus.client.feature.sewing.presentation.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.kenkoro.taurus.client.feature.sewing.data.source.remote.dto.request.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
  pager: Pager<Int, Order>
) : ViewModel() {
  val orderPagingFlow = pager
    .flow
    .cachedIn(viewModelScope)
}