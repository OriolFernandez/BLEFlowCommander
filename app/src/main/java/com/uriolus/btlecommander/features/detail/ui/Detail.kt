package com.uriolus.btlecommander.features.detail.ui

import Loading
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.uriolus.btlecommander.features.detail.DetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uriolus.btlecommander.features.detail.DetailState
import androidx.compose.runtime.getValue
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme

@Composable
fun Detail(mac: String, navigateUp: () -> Unit, viewModel: DetailViewModel) {
    viewModel.connectDevice(mac)
    val stateObservable: DetailState by viewModel.state.collectAsState()
    BTLECommanderTheme {
        Column() {
            when (val state = stateObservable) {
                is DetailState.Loaded -> DetailDevice(state.device)
                DetailState.Loading -> Loading()
            }
            Button(onClick = navigateUp) {
                Text(text = "Back")

            }
        }

    }
}