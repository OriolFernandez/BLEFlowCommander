package com.uriolus.btlecommander.navigation

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val selectedDevice: (String) -> Unit = { deviceID: String ->
        navController.navigate("${Destinations.DEVICE_DETAIL_ROUTE}/$deviceID")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}