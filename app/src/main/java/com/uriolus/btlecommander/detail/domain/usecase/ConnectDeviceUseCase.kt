package com.uriolus.btlecommander.detail.domain.usecase

import arrow.core.Either
import com.uriolus.btlelib.common.domain.BLEDevice
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class ConnectDeviceUseCase(
    private val bleRepository: BLEConnectRepository,
) {

    suspend fun exec(device: BLEDevice): Either<ConnectBLEDeviceError, Unit> {
       return bleRepository.connect(device)
    }

}