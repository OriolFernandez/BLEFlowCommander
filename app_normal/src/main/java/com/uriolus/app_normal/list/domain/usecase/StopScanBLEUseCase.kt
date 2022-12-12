package com.uriolus.app_normal.list.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository

class StopScanBLEUseCase(private val repository: BLEScanRepository) {
     fun exec() {
        return repository.stopScan()
    }
}