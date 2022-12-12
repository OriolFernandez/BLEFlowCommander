package com.uriolus.btlecommander.features.detail.ui

import UiForLoading
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.uriolus.btlecommander.features.detail.DetailState
import com.uriolus.btlecommander.features.detail.DetailViewModel
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    mac: String,
    navigateUp: () -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {

    val stateObservable: DetailState by remember(viewModel) { viewModel.state }.collectAsState()
    println("State in detail recomposing state= $stateObservable viewModel=$viewModel")
    BTLECommanderTheme {
        Column {
            println("State in detail:${stateObservable}")
            when (val state = stateObservable) {
                is DetailState.Loaded -> DetailDevice(state.device)
                DetailState.Loading -> {
                    println("State in detail is loading state")
                    UiForLoading()
                }
                is DetailState.Error -> println("Error: ${state.msg}")
                DetailState.Idle -> LaunchedEffect(Unit) {
                    println("State in detail, since is Idle then lets connect")
                    viewModel.connectDevice(mac)
                }
            }
            Button(onClick = navigateUp) {
                Text(text = "Back")
            }
        }

    }
}