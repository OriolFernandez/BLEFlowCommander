package com.uriolus.btlecommander.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uriolus.btlecommander.MainScreen
import com.uriolus.btlecommander.detail.ui.Detail
import com.uriolus.btlecommander.navigation.Destinations.DEVICES_ROUTE
import com.uriolus.btlecommander.navigation.Destinations.DEVICE_DETAIL_ID_KEY
import com.uriolus.btlecommander.navigation.Destinations.DEVICE_DETAIL_ROUTE

private const val ARGUMENT_NOT_SET = ""

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = DEVICES_ROUTE) {
        composable(DEVICES_ROUTE) {
            MainScreen(viewModel())
        }
        composable(
            "${DEVICE_DETAIL_ROUTE}/{$DEVICE_DETAIL_ID_KEY}",
            arguments = listOf(
                navArgument(DEVICE_DETAIL_ID_KEY) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            Detail(
                mac = arguments.getString(DEVICE_DETAIL_ID_KEY) ?: ARGUMENT_NOT_SET
            )
        }
    }
}