package com.uriolus.app_normal.list.domain.usecase

import com.uriolus.btlelib.scan.repository.BLEScanRepository

class StartScanBLEUseCase(private val repository: BLEScanRepository) {
     fun exec() {
         repository.startScan()
    }
}