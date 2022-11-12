package com.uriolus.btlecommander

import BLEDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.domain.usecase.ScanBLEUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val scanBLEUseCase: ScanBLEUseCase,
) : ViewModel() {
    private val _scanStatus = MutableStateFlow<ScanStatus>(ScanStatus.Idle)
    val scanStatus: StateFlow<ScanStatus>
        get() = _scanStatus

    fun scan() {
        _scanStatus.value = ScanStatus.Scanning
        viewModelScope.launch {
            val result = scanBLEUseCase.exec()
            _scanStatus.value = ScanStatus.Scanned(result)
        }

    }
}

sealed class ScanStatus {
    object Idle : ScanStatus()
    object Scanning : ScanStatus()
    data class Scanned(val devices: List<BLEDevice>) : ScanStatus()
}
