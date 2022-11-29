package com.uriolus.btlelib.connect.repository.impl

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import arrow.core.flatMap
import com.uriolus.btlelib.common.BLEDevicesCache
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.connect.data.BLEConnectDataSource
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class BLEConnectRepositoryData(
    private val dataSource: BLEConnectDataSource,
    private val bleDevicesCache: BLEDevicesCache
) : BLEConnectRepository {
    override fun connect(device: BLEDevice): Either<ConnectBLEDeviceError, Unit> {
        return bleDevicesCache.getBluetoothDevice(device.mac)
            .mapLeft { ConnectBLEDeviceError.DeviceNotFound(device.mac) }
            .flatMap { bluetoothDevice: BluetoothDevice -> dataSource.connect(bluetoothDevice) }
    }
}