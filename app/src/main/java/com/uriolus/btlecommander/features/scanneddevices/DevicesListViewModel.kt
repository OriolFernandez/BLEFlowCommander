package com.uriolus.btlecommander.features.scanneddevices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.domain.usecase.*
import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.scan.domain.ScanStatus
import com.uriolus.btlelib.statemonitor.domain.BluetoothState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch


class DevicesListViewModel(
    private val connectToScanBLEUseCase: ConnectToScanBLEUseCase,
    private val startScanBLEUseCase: StartScanBLEUseCase,
    private val stopScanBLEUseCase: StopScanBLEUseCase,
    private val registerToBluetoothStateUseCase: RegisterToBluetoothStateUseCase,
    private val unregisterToBluetoothStateUseCase: UnregisterToBluetoothStateUseCase,
    private val connectToBLEDeviceUseCase: ConnectToBLEDeviceUseCase
) : ViewModel() {
    private val _scanStatus = MutableStateFlow<PresentationScanState>(PresentationScanState.Idle)
    val scanStatus: StateFlow<PresentationScanState>
        get() = _scanStatus

    private val _navigationStatus = MutableStateFlow<NavigationState>(NavigationState.List)
    val navigationStatus = _navigationStatus.asStateFlow()

    init {
        subscribeToScan()
        subscribeToBluetoothState()
    }

    private fun subscribeToBluetoothState() {
        viewModelScope.launch {
            registerToBluetoothStateUseCase.exec().collect { bluetoothState ->
                when (bluetoothState) {
                    BluetoothState.Connected -> println("Bluetooth connected")
                    BluetoothState.Disconnected -> println("Bluetooth disconnected")
                    BluetoothState.Unknown -> println("Bluetooth state unknown")
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
                        is ScanStatus.ScanningDeviceFound -> {
                            devicesFound.clear()
                            devicesFound.addAll(it.devices)
                            _scanStatus.value =
                                PresentationScanState.ScanningDeviceFound(it.devices)
                        }
                        is ScanStatus.Error -> _scanStatus.value = PresentationScanState.Error(it)
                        is ScanStatus.ScanFinished -> _scanStatus.value =
                            PresentationScanState.Scanned(devicesFound)
                        is ScanStatus.Stopped -> _scanStatus.value = PresentationScanState.Idle
                        ScanStatus.ScanningStarted -> _scanStatus.value =
                            PresentationScanState.Scanning
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

    fun onDeviceClick(it: BLEDevicePresentation) {
        println("Device clicked $it")
        _navigationStatus.tryEmit(NavigationState.Detail(it))
    }

    private fun BLEDevicePresentation.toDomain(): BLEDevice =
        BLEDevice(this.name, this.mac, this.rssi)
}

sealed class PresentationScanState {
    object Idle : PresentationScanState()
    object Scanning : PresentationScanState()
    data class ScanningDeviceFound(val devices: List<BLEDevice>) : PresentationScanState()
    data class Scanned(val devices: List<BLEDevice>) : PresentationScanState()
    data class Error(val error: ScanStatus.Error) : PresentationScanState()
}

sealed class NavigationState() {
    object List : NavigationState()
    data class Detail(val device: BLEDevicePresentation) : NavigationState()
}