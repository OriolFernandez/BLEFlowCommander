package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository
import com.uriolus.btlelib.scan.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class ConnectToBLEDeviceUseCase(private val repository: BLEScanRepository) {
     fun exec(): StateFlow<ScanStatus> {
        return repository.connectToScanStatus()
    }
}