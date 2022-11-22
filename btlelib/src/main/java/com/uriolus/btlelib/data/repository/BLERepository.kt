package com.uriolus.btlelib.data.repository

import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

interface BLERepository {
    fun connectToScanStatus(): StateFlow<ScanStatus>
    fun startScan()
    fun stopScan()
}