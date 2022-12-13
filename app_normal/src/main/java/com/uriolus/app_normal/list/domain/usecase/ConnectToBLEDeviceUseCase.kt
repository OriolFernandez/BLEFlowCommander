package com.uriolus.app_normal.list.domain.usecase

import arrow.core.Either
import com.uriolus.btlelib.common.domain.model.BLEDevice
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class ConnectToBLEDeviceUseCase(private val repository: BLEConnectRepository) {
    suspend fun exec(device: BLEDevice): Either<ConnectBLEDeviceError, Unit> =
        repository.connect(device)
}
