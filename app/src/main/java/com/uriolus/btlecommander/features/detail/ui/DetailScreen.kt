package com.uriolus.btlecommander.features.detail.ui

import Loading
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.uriolus.btlecommander.features.detail.DetailViewModel
import com.uriolus.btlecommander.features.detail.DetailState
import androidx.compose.runtime.getValue
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme

@Composable
fun Detail(mac: String, navigateUp: () -> Unit, viewModel: DetailViewModel) {
    val stateObservable: DetailState by viewModel.state.collectAsState()
    BTLECommanderTheme {
        Column {
            println("State:${stateObservable}")
            when (val state = stateObservable) {
                is DetailState.Loaded -> DetailDevice(state.device)
                DetailState.Loading -> Loading()
                is DetailState.Error -> println("Error: ${state.msg}")
                DetailState.Idle -> LaunchedEffect(Unit) { viewModel.connectDevice(mac) }
            }
            Button(onClick = navigateUp) {
                Text(text = "Back")
            }
        }

    }
}