package com.uriolus.btlelib.di

import com.uriolus.btlelib.data.datasource.BLEScanDataSource
import com.uriolus.btlelib.data.datasource.impl.BLEScanScanDataSourceImpl
import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.data.repository.impl.BLERepositoryImpl
import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitor
import com.uriolus.btlelib.statemonitor.service.BluetoothStateMonitorAndroid
import org.koin.dsl.module

val btleLibModule = module {
    single<BLEScanDataSource> {
        BLEScanScanDataSourceImpl(get())
    }
    factory<BLERepository> {
        BLERepositoryImpl(get())
    }
    single<BluetoothStateMonitor> {
        BluetoothStateMonitorAndroid(get())
    }
}