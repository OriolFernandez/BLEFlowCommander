package com.uriolus.btlecommander.navigation

import DevicesListScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uriolus.btlecommander.features.detail.ui.DetailScreen
import com.uriolus.btlecommander.navigation.Destinations.DEVICES_ROUTE
import com.uriolus.btlecommander.navigation.Destinations.DEVICE_DETAIL_ID_KEY
import com.uriolus.btlecommander.navigation.Destinations.DEVICE_DETAIL_ROUTE
import org.koin.androidx.compose.koinViewModel


private const val ARGUMENT_NOT_SET = ""

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val actions: AppActions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = DEVICES_ROUTE
    ) {
        composable(DEVICES_ROUTE) {
            DevicesListScreen(koinViewModel(), actions.selectedDevice)
        }
        composable(
            "${DEVICE_DETAIL_ROUTE}/{$DEVICE_DETAIL_ID_KEY}",
            arguments = listOf(
                navArgument(DEVICE_DETAIL_ID_KEY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            println ("State in detail, creating detail")
            DetailScreen(
                mac = arguments.getString(DEVICE_DETAIL_ID_KEY) ?: ARGUMENT_NOT_SET,
                navigateUp = actions.navigateUp,
                koinViewModel()
            )
        }
    }
}