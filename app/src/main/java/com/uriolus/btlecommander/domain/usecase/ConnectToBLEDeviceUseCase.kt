package com.uriolus.btlecommander.domain.usecase

import arrow.core.Either
import com.uriolus.btlecommander.scanneddevices.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class ConnectToBLEDeviceUseCase(private val repository: BLEConnectRepository) {
    suspend fun exec(device: BLEDevicePresentation): Either<ConnectBLEDeviceError, Unit> =
        repository.connect(device.toDomain())
}

fun BLEDevicePresentation.toDomain(): BLEDevice =
    BLEDevice(this.name, this.mac, this.rssi)