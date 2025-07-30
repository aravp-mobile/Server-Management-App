package dev.arvind.androidtask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.arvind.androidtask.presentation.screen.auth.LoginScreen
import dev.arvind.androidtask.presentation.screen.dashboard.DashboardScreen
import dev.arvind.androidtask.presentation.screen.provision.ProvisionScreen
import dev.arvind.androidtask.presentation.screen.serverDetail.ServerDetailScreen
import dev.arvind.androidtask.presentation.screen.serverList.ServerListScreen

@Composable
fun ServerNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNavigateToProvision = {
                    navController.navigate("provision")
                },
                onNavigateToServerList = {
                    navController.navigate("server_list")
                }
            )
        }

        composable("provision") {
            ProvisionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("server_list") {
            ServerListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onServerClick = { serverId ->
                    navController.navigate("server_detail/$serverId")
                }
            )
        }

        composable("server_detail/{serverId}") { backStackEntry ->
            val serverId = backStackEntry.arguments?.getString("serverId") ?: return@composable
            ServerDetailScreen(
                serverId = serverId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}