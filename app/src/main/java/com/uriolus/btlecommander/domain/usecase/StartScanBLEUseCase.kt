package com.uriolus.btlecommander.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository

class StartScanBLEUseCase(private val repository: BLEScanRepository) {
     fun exec() {
         repository.startScan()
    }
}