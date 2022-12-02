package com.uriolus.btlelib.connect.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import arrow.core.Either
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError

interface BLEConnectDataSource {
   suspend fun connect(device: BluetoothDevice): Either<ConnectBLEDeviceError, BluetoothGatt>
}