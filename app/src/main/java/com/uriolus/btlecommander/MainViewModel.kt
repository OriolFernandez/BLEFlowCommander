package com.uriolus.btlecommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.domain.usecase.StartScanBLEUseCase
import com.uriolus.btlelib.BLEDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import com.uriolus.btlelib.domain.ScanStatus


class MainViewModel(
    private val startScanBLEUseCase: StartScanBLEUseCase,
) : ViewModel() {
    private val _scanStatus = MutableStateFlow<PresentationScanStatus>(PresentationScanStatus.Idle)
    val scanStatus: StateFlow<PresentationScanStatus>
        get() = _scanStatus

    fun scan() {
        val devicesFound: MutableList<BLEDevice> = mutableListOf()
        _scanStatus.value = PresentationScanStatus.Scanning
        viewModelScope.launch {
            startScanBLEUseCase.exec()
                .onCompletion {
                    println("NEW DEVICES: $devicesFound")
                }
                .collect {
                    when (it) {
                        is ScanStatus.DeviceFound -> {
                            devicesFound.add(it.device)
                            println("NEW DEVICE ${it.device}")
                        }
                        is ScanStatus.Error -> _scanStatus.value = PresentationScanStatus.Error(it)
                        is ScanStatus.ScanFinished -> TODO()
                        is ScanStatus.Stopped -> _scanStatus.value = PresentationScanStatus.Idle
                    }
                }
        }
    }
}

sealed class PresentationScanStatus {
    object Idle : PresentationScanStatus()
    object Scanning : PresentationScanStatus()
    data class Scanned(val devices: List<BLEDevice>) : PresentationScanStatus()
    data class Error(val error: ScanStatus.Error) : PresentationScanStatus()
}
