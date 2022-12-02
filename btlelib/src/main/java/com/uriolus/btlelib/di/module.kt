package com.uriolus.btlelib.di

import com.uriolus.btlelib.common.BLEDevicesCache
import com.uriolus.btlelib.connect.data.BLEConnectDataSource
import com.uriolus.btlelib.connect.data.BLEConnectDatasourceAndroid
import com.uriolus.btlelib.connect.repository.BLEConnectRepository
import com.uriolus.btlelib.connect.repository.impl.BLEConnectRepositoryData
import com.uriolus.btlelib.scan.data.datasource.BLEScanDataSource
import com.uriolus.btlelib.scan.data.datasource.impl.BLEScanScanDataSourceImpl
import com.uriolus.btlelib.scan.repository.BLEScanRepository
import com.uriolus.btlelib.scan.repository.impl.BLEScanRepositoryData
import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitor
import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitorAndroid
import org.koin.dsl.module

val btleLibModule = module {
    single{
        BLEDevicesCache()
    }
    single<BLEScanDataSource> {
        BLEScanScanDataSourceImpl(get(), get())
    }
    factory<BLEScanRepository> {
        BLEScanRepositoryData(get())
    }
    single<BluetoothStateMonitor> {
        BluetoothStateMonitorAndroid(get())
    }
    single<BLEConnectDataSource> {
        BLEConnectDatasourceAndroid(get())
    }
    factory<BLEConnectRepository> { BLEConnectRepositoryData(get(), get()) }
}