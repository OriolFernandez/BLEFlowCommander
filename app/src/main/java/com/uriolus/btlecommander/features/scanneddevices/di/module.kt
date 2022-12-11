package com.uriolus.btlecommander.features.scanneddevices.di

import com.uriolus.btlecommander.features.scanneddevices.DevicesListViewModel
import com.uriolus.btlecommander.domain.usecase.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureDevicesModule = module {
    viewModel {
        DevicesListViewModel(
            ConnectToScanBLEUseCase(get()),
            StartScanBLEUseCase(get()),
            StopScanBLEUseCase(get()),
            RegisterToBluetoothStateUseCase(get()),
            UnregisterToBluetoothStateUseCase(get()),
            ConnectToBLEDeviceUseCase(get())
        )
    }
}