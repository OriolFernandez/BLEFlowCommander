package com.uriolus.btlecommander.di

import com.uriolus.btlecommander.MainViewModel
import com.uriolus.btlecommander.domain.usecase.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        MainViewModel(
            ConnectToScanBLEUseCase(get()),
            StartScanBLEUseCase(get()),
            StopScanBLEUseCase(get()),
            RegisterToBluetoothStateUseCase(get()),
            UnregisterToBluetoothStateUseCase(get())
        )
    }
}