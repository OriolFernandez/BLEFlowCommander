package com.uriolus.app_normal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.app_normal.list.domain.usecase.ConnectToScanBLEUseCase
import com.uriolus.app_normal.list.domain.usecase.RegisterToBluetoothStateUseCase
import com.uriolus.app_normal.list.domain.usecase.StartScanBLEUseCase
import com.uriolus.app_normal.list.domain.usecase.StopScanBLEUseCase
import com.uriolus.app_normal.list.models.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.model.BLEDevice
import com.uriolus.btlelib.scan.domain.ScanStatus
import com.uriolus.btlelib.statemonitor.domain.BluetoothState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class DevicesListViewModel(
    private val connectToScanBLEUseCase: ConnectToScanBLEUseCase,
    private val startScanBLEUseCase: StartScanBLEUseCase,
    private val stopScanBLEUseCase: StopScanBLEUseCase,
    private val registerToBluetoothStateUseCase: RegisterToBluetoothStateUseCase,
) : ViewModel() {
    private val _scanStatus = MutableStateFlow<PresentationScanState>(PresentationScanState.Idle)
    val scanStatus: StateFlow<PresentationScanState> = _scanStatus.asStateFlow()

    private val _navigationStatus = MutableStateFlow<NavigationState>(NavigationState.List)
    val navigationStatus: StateFlow<NavigationState> = _navigationStatus.asStateFlow()

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
                    println("State in Devices NEW STATE $it")
                    when (val scanStatus = it) {
                        is ScanStatus.ScanningDeviceFound -> {
                            devicesFound.clear()
                            devicesFound.addAll(scanStatus.devices)
                            _scanStatus.update {
                                PresentationScanState.ScanningDeviceFound(scanStatus.devices)
                            }
                        }
                        is ScanStatus.Error -> _scanStatus.update {
                            PresentationScanState.Error(scanStatus)
                        }
                        is ScanStatus.ScanFinished -> {
                            _scanStatus.update { PresentationScanState.Scanned(devicesFound) }
                        }
                        is ScanStatus.Stopped -> _scanStatus.update { PresentationScanState.Idle }
                        ScanStatus.ScanningStarted -> _scanStatus.update {
                            PresentationScanState.Scanning
                        }
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