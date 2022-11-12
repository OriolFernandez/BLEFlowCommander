package com.uriolus.btlecommander.di

import com.uriolus.btlecommander.MainViewModel
import com.uriolus.btlecommander.domain.usecase.ScanBLEUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        MainViewModel(ScanBLEUseCase())
    }
}