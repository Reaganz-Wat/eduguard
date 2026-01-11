package com.example.edugard.ui.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edugard.ui.admin.analytics.AnalyticsScreen
import com.example.edugard.ui.admin.dashboard.AdminDashboardScreen
import com.example.edugard.ui.admin.login.AdminLoginScreen
import com.example.edugard.ui.admin.students.StudentManagementScreen
import com.example.edugard.ui.components.AppNavigationDrawer
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    // Admin routes
    object AdminLogin : Screen("admin_login")
    object AdminDashboard : Screen("admin_dashboard")
    object ManageStudents : Screen("manage_students")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
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
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            AppNavigationDrawer(
                drawerState = drawerState,
                currentRoute = Screen.AdminDashboard.route,
                onNavigate = { route ->
                    scope.launch {
                        drawerState.close()
                        if (route != Screen.AdminDashboard.route) {
                            navController.navigate(route)
                        }
                    }
                },
                onLogout = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Screen.AdminLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                AdminDashboardScreen(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onProfileClick = {
                        // TODO: Navigate to profile
                    },
                    onViewAnalytics = {
                        navController.navigate(Screen.Analytics.route)
                    },
                    onManageStudents = {
                        navController.navigate(Screen.ManageStudents.route)
                    }
                )
            }
        }

        composable(Screen.ManageStudents.route) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            AppNavigationDrawer(
                drawerState = drawerState,
                currentRoute = Screen.ManageStudents.route,
                onNavigate = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        popUpTo(Screen.AdminDashboard.route)
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Screen.AdminLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                StudentManagementScreen(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onProfileClick = {
                        // TODO: Navigate to profile
                    }
                )
            }
        }

        composable(Screen.Analytics.route) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            AppNavigationDrawer(
                drawerState = drawerState,
                currentRoute = Screen.Analytics.route,
                onNavigate = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        popUpTo(Screen.AdminDashboard.route)
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Screen.AdminLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                AnalyticsScreen(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onProfileClick = {
                        // TODO: Navigate to profile
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
