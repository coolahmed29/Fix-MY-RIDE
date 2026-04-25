package com.example.fix_my_ride.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fix_my_ride.Dashboards.user_dashboard.presentation.view.DashboardScreen
import com.example.fix_my_ride.Features.Authentication.Presentation.View.LoginScreen
import com.example.fix_my_ride.Features.Authentication.Presentation.View.RegisterScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.view.PartDetailScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.view.SparePartsScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.SparePartsViewModel

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

        composable<SparePartsScreen> {
            val viewModel = hiltViewModel<SparePartsViewModel>()

            SparePartsScreen(
                onPartClick = { part ->
                    viewModel.onPartSelect(part)
                    navController.navigate(PartDetailScreen)
                },
                onBack    = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

// ── Part Detail ───────────────────────────────
        composable<PartDetailScreen> {

            // ✅ 'it' key add karo — warning fix
            val sparePartsEntry = remember(it) {
                navController.getBackStackEntry<SparePartsScreen>()
            }
            val viewModel = hiltViewModel<SparePartsViewModel>(sparePartsEntry)

            val part = viewModel.selectedPart
                .collectAsStateWithLifecycle().value

            if (part != null) {
                PartDetailScreen(
                    part      = part,
                    onBack    = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }


        composable<UserDashboard> {
            DashboardScreen(
                onNavigateToSpareParts = {
                    navController.navigate(SparePartsScreen)
                }
            )
        }
    }
}