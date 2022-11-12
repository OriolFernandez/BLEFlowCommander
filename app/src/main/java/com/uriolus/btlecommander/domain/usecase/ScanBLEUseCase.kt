package com.uriolus.btlecommander.domain.usecase

import BLEDevice
import kotlinx.coroutines.delay

class ScanBLEUseCase {
    suspend fun exec():List<BLEDevice>{
        delay(2230)
        return listOf(BLEDevice("test1","mac:de:test"))
    }
}