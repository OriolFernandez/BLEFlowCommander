package com.uriolus.btlelib.connect.data

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError

interface BLEConnectDataSource {
    fun connect(device: BluetoothDevice): Either<ConnectBLEDeviceError, Unit>
}