package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.statemonitor.domain.BluetoothState
import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitor
import kotlinx.coroutines.flow.StateFlow

class RegisterToBluetoothStateUseCase(private val bluetoothStateMonitor: BluetoothStateMonitor) {
    fun exec(): StateFlow<BluetoothState>  = bluetoothStateMonitor.subscribe()
}