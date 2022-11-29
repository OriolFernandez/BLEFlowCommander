package com.uriolus.btlelib.scan.domain

import com.uriolus.btlelib.common.domain.BLEDevice

sealed class ScanStatus {
    object Stopped : ScanStatus()
    data class Scanning(val devices: List<BLEDevice>) : ScanStatus()
    object ScanFinished : ScanStatus()
    data class Error(val scanError: ScanError) : ScanStatus()
}
