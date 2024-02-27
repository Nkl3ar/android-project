package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopJobListViewmodel : ViewModel() {
    private val shopJobRepository = ShopJobRepository.get()

    private val _jobs: MutableStateFlow<List<ShopJob>> = MutableStateFlow(emptyList())
    val jobs: StateFlow<List<ShopJob>>
        get() = _jobs.asStateFlow()

    init {
        viewModelScope.launch {
            shopJobRepository.getShopJobs().collect {
                _jobs.value = it
            }
        }
    }

    suspend fun addShopJob(shopJob: ShopJob) {
        shopJobRepository.addShopJob(shopJob)
    }
}
