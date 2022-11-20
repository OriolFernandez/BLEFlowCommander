package com.uriolus.btlelib.di

import android.app.Activity
import com.uriolus.btlelib.data.datasource.BLEDataSource
import com.uriolus.btlelib.data.datasource.impl.BLEDataSourceImpl
import com.uriolus.btlelib.data.repository.BLERepository
import com.uriolus.btlelib.data.repository.impl.BLERepositoryImpl
import org.koin.dsl.module

val btlelibModule = module {
    single<BLEDataSource> {
        BLEDataSourceImpl(get())
    }
    factory<BLERepository> {
        BLERepositoryImpl(get())
    }
}