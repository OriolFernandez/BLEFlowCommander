package com.uriolus.btlelib.statemonitor.domain

sealed class BluetoothState {
    object Unknown : BluetoothState()
    object Disconnected : BluetoothState()
    object Connected : BluetoothState()
}
