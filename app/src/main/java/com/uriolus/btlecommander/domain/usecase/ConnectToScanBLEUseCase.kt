package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class ConnectToScanBLEUseCase(private val repository:BLERepository) {
     fun exec(): StateFlow<ScanStatus> {
        return repository.connectToScanStatus()
    }
}