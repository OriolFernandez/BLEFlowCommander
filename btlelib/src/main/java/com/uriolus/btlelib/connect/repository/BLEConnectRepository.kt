package com.uriolus.btlelib.connect.repository

import arrow.core.Either
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError

interface BLEConnectRepository {
    suspend fun connect(device: BLEDevice): Either<ConnectBLEDeviceError, Unit>
    suspend fun connectByMac(mac: String): Either<ConnectBLEDeviceError, Unit>
}