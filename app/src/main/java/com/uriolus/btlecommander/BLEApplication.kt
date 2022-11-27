package com.uriolus.btlecommander

import android.app.Application
import com.uriolus.btlecommander.di.featureModule
import com.uriolus.btlelib.di.btleLibModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BLEApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@BLEApplication)
            // Load modules
            modules(featureModule, btleLibModule)
        }
    }
}