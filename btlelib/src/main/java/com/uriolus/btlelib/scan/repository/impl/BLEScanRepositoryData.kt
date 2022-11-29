package com.uriolus.btlelib.scan.repository.impl

import com.uriolus.btlelib.scan.data.datasource.BLEScanDataSource
import com.uriolus.btlelib.scan.repository.BLEScanRepository
import com.uriolus.btlelib.scan.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class BLEScanRepositoryData(private val dataSource: BLEScanDataSource) : BLEScanRepository {
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