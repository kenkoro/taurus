package com.kenkoro.taurus.client.feature.shared.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailsContext
import com.kenkoro.taurus.client.feature.search.order.details.presentation.util.detail.OrderDetailsSearchFactory
import com.kenkoro.taurus.client.feature.shared.states.TaurusTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedOrderDetailsSearchViewModel
  @Inject
  constructor() : ViewModel() {
    private val searchContext = OrderDetailsContext()
    var selectedDropDown by mutableStateOf<TaurusTextFieldState?>(null)
      private set

    fun selectDropDown(dropDownState: TaurusTextFieldState) {
      selectedDropDown = dropDownState
      changeSearchBehavior(dropDownState)
    }

    private fun changeSearchBehavior(dropDownState: TaurusTextFieldState) {
      val strategy = OrderDetailsSearchFactory.getOrderDetailSearchStrategy(dropDownState)
      searchContext.strategy(strategy)
    }

    suspend fun fetch(searchRequest: String): List<String> = searchContext.fetch(searchRequest)
  }