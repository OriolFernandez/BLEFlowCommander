package com.uriolus.btlelib.data.repository.impl

import com.uriolus.btlelib.data.datasource.BLEDataSource
import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.StateFlow

class BLERepositoryImpl(private val dataSource: BLEDataSource) : BLERepository {
    override fun startScan(): StateFlow<ScanStatus> {
       return dataSource.startScan()
    }

    override fun stopScan() {
        dataSource.stopScan()
    }
}