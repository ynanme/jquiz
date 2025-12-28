package com.example.jquiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.services.VipService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val vipService: VipService) : ViewModel() {

    val isVip = vipService.vipFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun toggleVip(newValue: Boolean) {
        viewModelScope.launch {
            vipService.setVip(newValue)
        }
    }
}
