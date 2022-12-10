package com.uriolus.btlecommander.detail.ui

import Loading
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.uriolus.btlecommander.detail.DetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uriolus.btlecommander.detail.DetailState
import androidx.compose.runtime.getValue
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme

@Composable
fun Detail(viewModel: DetailViewModel = viewModel()) {
    val stateObservable: DetailState by viewModel.state.collectAsState()
    BTLECommanderTheme {
        when (val state = stateObservable) {
            is DetailState.Loaded -> DetailDevice(state.device)
            DetailState.Loading -> Loading()
        }
    }
}