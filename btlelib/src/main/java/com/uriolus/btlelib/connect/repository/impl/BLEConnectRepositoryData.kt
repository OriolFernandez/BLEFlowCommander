package com.uriolus.btlelib.connect.repository.impl

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.uriolus.btlelib.common.BLEDevicesCache
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.connect.data.BLEConnectDataSource
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class BLEConnectRepositoryData(
    private val dataSource: BLEConnectDataSource,
    private val bleDevicesCache: BLEDevicesCache
) : BLEConnectRepository {
    override suspend fun connect(device: BLEDevice): Either<ConnectBLEDeviceError, Unit> {
        return bleDevicesCache.getBluetoothDevice(device.mac)
            .mapLeft { ConnectBLEDeviceError.DeviceNotFound(device.mac) }
            .flatMap { bluetoothDevice: BluetoothDevice -> dataSource.connect(bluetoothDevice) }
            .flatMap { Unit.right() }
    }
    override suspend fun connectByMac(mac: String): Either<ConnectBLEDeviceError, Unit> {
        return bleDevicesCache.getBluetoothDevice(mac)
            .mapLeft { ConnectBLEDeviceError.DeviceNotFound(mac) }
            .flatMap { bluetoothDevice: BluetoothDevice -> dataSource.connect(bluetoothDevice) }
            .flatMap { Unit.right() }
    }
}