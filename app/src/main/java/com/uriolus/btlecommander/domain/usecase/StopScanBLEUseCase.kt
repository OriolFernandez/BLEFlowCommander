package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class StopScanBLEUseCase(private val repository:BLERepository) {
     fun exec() {
        return repository.stopScan()
    }
}