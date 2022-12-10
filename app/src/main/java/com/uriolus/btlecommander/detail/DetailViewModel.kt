package com.uriolus.btlecommander.detail

import androidx.lifecycle.ViewModel
import com.uriolus.btlecommander.detail.domain.usecase.ConnectDeviceUseCase
import com.uriolus.btlecommander.scanneddevices.BLEDevicePresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel(val connectDeviceUseCase: ConnectDeviceUseCase) : ViewModel() {
    private val _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun connectDevice(mac:String){

    }
}

sealed class DetailState {
    object Loading : DetailState()
    data class Loaded(val device: BLEDevicePresentation) : DetailState()
}