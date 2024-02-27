package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopManagerListViewmodel : ViewModel() {
    private val shopManagerRepository = ShopManagerRepository.get()

    private val _shopManagers: MutableStateFlow<List<ShopManager>> = MutableStateFlow(emptyList())
    val shopManagers: StateFlow<List<ShopManager>>
        get() = _shopManagers.asStateFlow()

    init {
        viewModelScope.launch {
            shopManagerRepository.getShopManagers().collect {
                _shopManagers.value = it
            }
        }
    }

    suspend fun addShopManager(shopManager: ShopManager) {
        shopManagerRepository.addShopManager(shopManager)
    }
}
