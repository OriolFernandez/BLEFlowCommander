package com.uriolus.btlelib.statemonitor.service

import com.uriolus.btlelib.statemonitor.domain.BluetoothState
import kotlinx.coroutines.flow.StateFlow

interface BluetoothStateMonitor {
    fun subscribe(): StateFlow<BluetoothState>
    fun unsubscribe()
}
