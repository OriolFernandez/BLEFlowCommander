package com.uriolus.btlelib.scan.data.datasource

import com.uriolus.btlelib.scan.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

interface BLEScanDataSource {
    fun connectToScanStatus():StateFlow<ScanStatus>
    fun startScan()
    fun stopScan()
}