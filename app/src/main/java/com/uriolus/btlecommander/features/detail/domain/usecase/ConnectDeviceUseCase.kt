package com.uriolus.btlecommander.features.detail.domain.usecase

import arrow.core.Either
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import com.uriolus.btlelib.connect.repository.BLEConnectRepository

class ConnectDeviceUseCase(
    private val bleRepository: BLEConnectRepository,
) {

    suspend fun exec(mac: String): Either<ConnectBLEDeviceError, Unit> {
       return bleRepository.connectByMac(mac)
    }

}