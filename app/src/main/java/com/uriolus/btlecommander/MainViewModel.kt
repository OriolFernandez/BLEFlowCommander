package com.uriolus.btlecommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.domain.usecase.*
import com.uriolus.btlelib.BLEDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import com.uriolus.btlelib.domain.ScanStatus
import com.uriolus.btlelib.statemonitor.domain.BluetoothState


class MainViewModel(
    private val connectToScanBLEUseCase: ConnectToScanBLEUseCase,
    private val startScanBLEUseCase: StartScanBLEUseCase,
    private val stopScanBLEUseCase: StopScanBLEUseCase,
    private val registerToBluetoothStateUseCase: RegisterToBluetoothStateUseCase,
    private val unregisterToBluetoothStateUseCase: UnregisterToBluetoothStateUseCase
) : ViewModel() {
    private val _scanStatus = MutableStateFlow<PresentationScanStatus>(PresentationScanStatus.Idle)
    val scanStatus: StateFlow<PresentationScanStatus>
        get() = _scanStatus

    init {
        subscribeToScan()
        subscribeToBluetoothState()
    }

    private fun subscribeToBluetoothState() {
        viewModelScope.launch {
            registerToBluetoothStateUseCase.exec().collect {bluetoothState->
                when(bluetoothState){
                    BluetoothState.Connected -> println("Bluetooth connected")
                    BluetoothState.Disconnected ->  println("Bluetooth disconnected")
                    BluetoothState.Unknown ->  println("Bluetooth state unknown")
                }
            }
        }
    }

    private fun subscribeToScan() {
        val devicesFound: MutableList<BLEDevice> = mutableListOf()
        viewModelScope.launch {
            connectToScanBLEUseCase.exec()
                .onCompletion {
                    println("NEW DEVICES!!!!!!!!!: $devicesFound")
                }
                .collect {
                    println("NEW STATE $it")
                    when (it) {
                        is ScanStatus.Scanning -> {
                            devicesFound.addAll(it.devices)
                            _scanStatus.value = PresentationScanStatus.Scanning
                        }
                        is ScanStatus.Error -> _scanStatus.value = PresentationScanStatus.Error(it)
                        is ScanStatus.ScanFinished -> _scanStatus.value =
                            PresentationScanStatus.Idle
                        is ScanStatus.Stopped -> _scanStatus.value = PresentationScanStatus.Idle
                    }
                }
        }
    }

    fun startScan() {
        viewModelScope.launch {
            startScanBLEUseCase.exec()
        }
    }

    fun stopScan() {
        viewModelScope.launch {
            stopScanBLEUseCase.exec()
        }
    }
}

sealed class PresentationScanStatus {
    object Idle : PresentationScanStatus()
    object Scanning : PresentationScanStatus()
    data class Scanned(val devices: List<BLEDevice>) : PresentationScanStatus()
    data class Error(val error: ScanStatus.Error) : PresentationScanStatus()
}
