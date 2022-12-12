package com.uriolus.app_normal.list.domain.usecase

import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitor

class UnregisterToBluetoothStateUseCase(private val bluetoothStateMonitor: BluetoothStateMonitor) {
    fun exec() = bluetoothStateMonitor.unsubscribe()
}