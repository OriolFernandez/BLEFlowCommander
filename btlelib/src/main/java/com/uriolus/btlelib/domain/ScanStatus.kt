package com.uriolus.btlelib.domain

import com.uriolus.btlelib.BLEDevice

sealed class ScanStatus {
    object Stopped : ScanStatus()
    data class Scanning(val devices: List<BLEDevice>) : ScanStatus()
    data class ScanFinished(val devices: List<BLEDevice>) : ScanStatus()
    data class Error(val scanError: ScanError) : ScanStatus()
}
