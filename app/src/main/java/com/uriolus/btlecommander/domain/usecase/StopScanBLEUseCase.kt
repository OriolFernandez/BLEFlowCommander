package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository

class StopScanBLEUseCase(private val repository: BLEScanRepository) {
     fun exec() {
        return repository.stopScan()
    }
}