package com.uriolus.btlelib.connect.domain

sealed class ConnectBLEDeviceError {
    data class DeviceNotFound(val address: String) : ConnectBLEDeviceError()
}
