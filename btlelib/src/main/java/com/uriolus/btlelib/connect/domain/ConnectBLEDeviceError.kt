package com.uriolus.btlelib.connect.domain

sealed class ConnectBLEDeviceError {
    data class DeviceNotFound(val address: String) : ConnectBLEDeviceError()
    data class ConnectionCanceled(val message:String) : ConnectBLEDeviceError()
    object Disconnected:ConnectBLEDeviceError()
    data class BluetoothError(val message:String):ConnectBLEDeviceError()
}
