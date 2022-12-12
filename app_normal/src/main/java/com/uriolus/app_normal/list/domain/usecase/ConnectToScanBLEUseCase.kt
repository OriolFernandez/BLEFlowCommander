package com.uriolus.app_normal.list.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository
import com.uriolus.btlelib.scan.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class ConnectToScanBLEUseCase(private val repository: BLEScanRepository) {
     fun exec(): StateFlow<ScanStatus> {
        return repository.connectToScanStatus()
    }
}