package com.uriolus.btlecommander.features.scanneddevices.di

import com.uriolus.btlecommander.domain.usecase.ConnectToScanBLEUseCase
import com.uriolus.btlecommander.domain.usecase.RegisterToBluetoothStateUseCase
import com.uriolus.btlecommander.domain.usecase.StartScanBLEUseCase
import com.uriolus.btlecommander.domain.usecase.StopScanBLEUseCase
import com.uriolus.btlecommander.features.scanneddevices.DevicesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureDevicesModule = module {
    viewModel {
        DevicesListViewModel(
            ConnectToScanBLEUseCase(get()),
            StartScanBLEUseCase(get()),
            StopScanBLEUseCase(get()),
            RegisterToBluetoothStateUseCase(get()),
        )
    }
}