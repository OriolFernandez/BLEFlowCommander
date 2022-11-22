package com.uriolus.btlecommander.di

import com.uriolus.btlecommander.MainViewModel
import com.uriolus.btlecommander.domain.usecase.ConnectToScanBLEUseCase
import com.uriolus.btlecommander.domain.usecase.StartScanBLEUseCase
import com.uriolus.btlecommander.domain.usecase.StopScanBLEUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        MainViewModel(
            ConnectToScanBLEUseCase(get()),
            StartScanBLEUseCase(get()),
            StopScanBLEUseCase(get())
        )
    }
}