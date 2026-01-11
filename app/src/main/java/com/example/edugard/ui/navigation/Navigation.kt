package com.example.edugard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edugard.ui.admin.dashboard.AdminDashboardScreen
import com.example.edugard.ui.admin.login.AdminLoginScreen

sealed class Screen(val route: String) {
    // Admin routes
    object AdminLogin : Screen("admin_login")
    object AdminDashboard : Screen("admin_dashboard")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AdminLogin.route
    ) {
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.AdminDashboard.route) {
                        // Clear login screen from back stack
                        popUpTo(Screen.AdminLogin.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
