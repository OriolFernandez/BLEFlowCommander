package com.uriolus.btlecommander.features.detail.di

import com.uriolus.btlecommander.features.detail.DetailViewModel
import com.uriolus.btlecommander.features.detail.domain.usecase.ConnectDeviceUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureDetailModule = module {
    viewModel {
        DetailViewModel(
            ConnectDeviceUseCase(get())
        )
    }
}