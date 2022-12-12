package com.uriolus.btlecommander.navigation

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val selectedDevice: (String) -> Unit = { deviceID: String ->
        println("State in detail, device selected $deviceID")
        navController.navigate("${Destinations.DEVICE_DETAIL_ROUTE}/$deviceID")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}