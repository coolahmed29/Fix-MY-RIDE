package com.example.fix_my_ride.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fix_my_ride.Dashboards.user_dashboard.presentation.view.DashboardScreen
import com.example.fix_my_ride.Features.Authentication.Presentation.View.LoginScreen
import com.example.fix_my_ride.Features.Authentication.Presentation.View.RegisterScreen

@Composable
fun SystemNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RegistrationScreen
    ) {

        composable<RegistrationScreen> {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate(LoginScreen) {
                        popUpTo(RegistrationScreen) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginScreen)
                }
            )
        }

        composable<LoginScreen> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(UserDashboard)
                },
                onNavigateToRegister = {
                    navController.navigate(RegistrationScreen)
                },
                onBack = {
                    navController.popBackStack()
                }

            )
        }
        composable<UserDashboard>{
            DashboardScreen()
        }

    }
}