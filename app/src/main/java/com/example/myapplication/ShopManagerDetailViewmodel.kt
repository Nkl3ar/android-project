package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ShopManagerDetailViewModel(shopManagerId: UUID) : ViewModel() {
    private val shopManagerRepository = ShopManagerRepository.get()

    private val _shopmanager: MutableStateFlow<ShopManager?> = MutableStateFlow(null)
    val shopmanager: StateFlow<ShopManager?> = _shopmanager.asStateFlow()


    init {
        viewModelScope.launch {
            _shopmanager.value = shopManagerRepository.getShopManager(shopManagerId)
        }
    }

    fun updateShopManager(onUpdate: (ShopManager) -> ShopManager) {
        _shopmanager.update { oldShopManager ->
            oldShopManager?.let { onUpdate(it) }
        }
    }


    override fun onCleared() {
        super.onCleared()
        shopmanager.value?.let { shopManagerRepository.updateShopManager(it) }
    }
}

class ShopManagerDetailViewModelFactory(
    private val shopManagerId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopManagerDetailViewModel(shopManagerId) as T
    }
}