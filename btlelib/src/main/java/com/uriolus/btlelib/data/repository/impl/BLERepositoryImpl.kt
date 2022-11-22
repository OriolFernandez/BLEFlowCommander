package com.uriolus.btlelib.data.repository.impl

import com.uriolus.btlelib.data.datasource.BLEDataSource
import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class BLERepositoryImpl(private val dataSource: BLEDataSource) : BLERepository {
    override fun connectToScanStatus(): StateFlow<ScanStatus> {
        return dataSource.connectToScanStatus()
    }

    override fun startScan() {
        return dataSource.startScan()
    }

    override fun stopScan() {
        dataSource.stopScan()
    }
}