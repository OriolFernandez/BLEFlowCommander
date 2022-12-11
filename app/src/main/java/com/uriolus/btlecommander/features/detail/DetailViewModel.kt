package com.uriolus.btlecommander.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.features.detail.domain.usecase.ConnectDeviceUseCase
import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val connectDeviceUseCase: ConnectDeviceUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun connectDevice(mac: String) {
        viewModelScope.launch {
            connectDeviceUseCase.exec(mac)
                .fold({
                    _state.value = DetailState.Error(it.toString())
                }, {
                    println("Detail connected")
                })
        }
    }
}

sealed class DetailState {
    object Loading : DetailState()
    data class Loaded(val device: BLEDevicePresentation) : DetailState()
    data class Error(val msg: String) : DetailState()
}