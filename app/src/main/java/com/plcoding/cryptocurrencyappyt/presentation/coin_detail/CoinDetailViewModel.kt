package com.plcoding.cryptocurrencyappyt.presentation.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptocurrencyappyt.common.Constants
import com.plcoding.cryptocurrencyappyt.common.Resource
import com.plcoding.cryptocurrencyappyt.domain.use_case.GetCoinUsercase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUsercase: GetCoinUsercase,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
        }
    }

    private fun getCoin(coinId: String){
        getCoinUsercase(coinId).onEach { result ->
            when(result){
                is Resource.Success ->{
                    _state.value = CoinDetailState(coin = result.data ?: null)
                }
                is Resource.Error ->{
                    _state.value = CoinDetailState(error = result.message.toString())
                }
                is Resource.Loading ->{
                    _state.value = CoinDetailState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }

}