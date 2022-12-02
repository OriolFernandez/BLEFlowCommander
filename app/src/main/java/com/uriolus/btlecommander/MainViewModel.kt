package com.uriolus.btlecommander

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uriolus.btlecommander.domain.usecase.*
import com.uriolus.btlecommander.scanneddevices.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.BLEDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import com.uriolus.btlelib.scan.domain.ScanStatus
import com.uriolus.btlelib.statemonitor.domain.BluetoothState


class MainViewModel(
    private val connectToScanBLEUseCase: ConnectToScanBLEUseCase,
    private val startScanBLEUseCase: StartScanBLEUseCase,
    private val stopScanBLEUseCase: StopScanBLEUseCase,
    private val registerToBluetoothStateUseCase: RegisterToBluetoothStateUseCase,
    private val unregisterToBluetoothStateUseCase: UnregisterToBluetoothStateUseCase,
    private val connectToBLEDeviceUseCase: ConnectToBLEDeviceUseCase
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
                            _scanStatus.value = PresentationScanStatus.ScanningDeviceFound(it.devices)
                        }
                        is ScanStatus.Error -> _scanStatus.value = PresentationScanStatus.Error(it)
                        is ScanStatus.ScanFinished -> _scanStatus.value =
                            PresentationScanStatus.Scanned(devicesFound)
                        is ScanStatus.Stopped -> _scanStatus.value = PresentationScanStatus.Idle
                        ScanStatus.ScanningStarted -> _scanStatus.value = PresentationScanStatus.Scanning
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
        // connectToScanBLEUseCase.exec()
        viewModelScope.launch {
            connectToBLEDeviceUseCase.exec(it)
                .fold({
                    println("error $it")
                    println("Thread ${Thread.currentThread()}")
                },{
                    println("Connected")
                    println("Thread ${Thread.currentThread()}")
                })
        }
    }
}

sealed class PresentationScanStatus {
    object Idle : PresentationScanStatus()
    object  Scanning:PresentationScanStatus()
    data class ScanningDeviceFound(val devices: List<BLEDevice>) : PresentationScanStatus()
    data class Scanned(val devices: List<BLEDevice>) : PresentationScanStatus()
    data class Error(val error: ScanStatus.Error) : PresentationScanStatus()
}
