package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ShopJobDetailViewModel(shopJobId: UUID) : ViewModel() {
    private val shopJobRepository = ShopJobRepository.get()

    private val _shopjob: MutableStateFlow<ShopJob?> = MutableStateFlow(null)
    val shopjob: StateFlow<ShopJob?> = _shopjob.asStateFlow()
    private lateinit var shopJobIdDel: UUID


    init {
        viewModelScope.launch {
            _shopjob.value = shopJobRepository.getShopJob(shopJobId)
            shopJobIdDel = shopJobId
        }
    }

    fun updateShopjob(onUpdate: (ShopJob) -> ShopJob) {
        _shopjob.update { oldShopjob ->
            oldShopjob?.let { onUpdate(it) }
        }
    }

    fun deleteShopjob() {
        GlobalScope.launch{
            shopJobRepository.delShopJob(shopJobRepository.getShopJob(shopJobIdDel))
        }


    }

    override fun onCleared() {
        super.onCleared()
        shopjob.value?.let { shopJobRepository.updateShopJob(it) }
    }
}

class ShopJobDetailViewModelFactory(
    private val shopJobId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopJobDetailViewModel(shopJobId) as T
    }
}