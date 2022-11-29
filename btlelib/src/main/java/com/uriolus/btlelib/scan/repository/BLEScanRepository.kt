package com.uriolus.btlelib.scan.repository

import com.uriolus.btlelib.scan.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

interface BLEScanRepository {
    fun connectToScanStatus(): StateFlow<ScanStatus>
    fun startScan()
    fun stopScan()
}