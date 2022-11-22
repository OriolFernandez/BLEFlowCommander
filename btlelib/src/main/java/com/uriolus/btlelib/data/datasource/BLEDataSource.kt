package com.uriolus.btlelib.data.datasource

import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

interface BLEDataSource {
    fun connectToScanStatus():StateFlow<ScanStatus>
    fun startScan()
    fun stopScan()
}