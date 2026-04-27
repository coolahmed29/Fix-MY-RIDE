// Navigation/SystemNavigation.kt
package com.example.fix_my_ride.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.presentation.view.BookingConfirmScreen
import com.example.fix_my_ride.Features.Detailing.presentation.view.BookingScreen
import com.example.fix_my_ride.Features.Detailing.presentation.view.PackageDetailScreen
import com.example.fix_my_ride.Features.Detailing.presentation.view.PackageListScreen
import com.example.fix_my_ride.Features.Detailing.presentation.view.ReviewScreen
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.BookingViewModel
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.PackageListViewModel
import com.example.fix_my_ride.Features.Authentication.Presentation.View.LoginScreen
import com.example.fix_my_ride.Features.Authentication.Presentation.View.RegisterScreen
import com.example.fix_my_ride.Dashboards.user_dashboard.presentation.view.DashboardScreen
import com.example.fix_my_ride.Features.Chat.presentation.view.ChatScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.view.PartDetailScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.view.SparePartsScreen
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.SparePartsViewModel

@Composable
fun SystemNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = RegistrationScreen
    ) {

        // ── Registration ──────────────────────────────
        composable<RegistrationScreen> {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate(UserDashboard) {
                        popUpTo<RegistrationScreen> { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginScreen)
                }
            )
        }

        // ── Login ─────────────────────────────────────
        composable<LoginScreen> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(UserDashboard) {
                        popUpTo<LoginScreen> { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegistrationScreen)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<UserDashboard> {
            DashboardScreen(
                onNavigateToSpareParts = {
                    navController.navigate(SparePartsScreen) {
                        launchSingleTop = true  // ✅ ADD THIS
                        restoreState = true      // ✅ ADD THIS
                    }
                },
                onNavigateToDetailing = {
                    navController.navigate(PackageListScreen) {
                        launchSingleTop = true  // ✅ ADD THIS
                        restoreState = true      // ✅ ADD THIS
                    }
                }
            )
        }

// ── Spare Parts ───────────────────────────────
        composable<SparePartsScreen> {
            val viewModel = hiltViewModel<SparePartsViewModel>()
            SparePartsScreen(
                onPartClick = { part ->
                    viewModel.onPartSelect(part)
                    navController.navigate(PartDetailScreen)
                },
                onBack = {
                    // ✅ ADD THIS - صاف طریقے سے واپس جاؤ
                    navController.navigate(UserDashboard) {
                        popUpTo<SparePartsScreen> { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

// ── Package List ──────────────────────────────
        composable<PackageListScreen> {
            val viewModel = hiltViewModel<PackageListViewModel>()
            PackageListScreen(
                onPackageClick = { pkg ->
                    viewModel.onPackageSelect(pkg)
                    navController.navigate(PackageDetailScreen)
                },
                onBack = {
                    // ✅ ADD THIS - صاف طریقے سے واپس جاؤ
                    navController.navigate(UserDashboard) {
                        popUpTo<PackageListScreen> { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        composable<PartDetailScreen> {
            val sparePartsEntry = remember(it) {
                navController.getBackStackEntry<SparePartsScreen>()
            }
            val viewModel = hiltViewModel<SparePartsViewModel>(sparePartsEntry)
            val part = viewModel.selectedPart
                .collectAsStateWithLifecycle().value

            if (part != null) {
                PartDetailScreen(
                    part        = part,
                    onBack      = { navController.popBackStack() },
                    onChatClick = { vendorId, vendorName ->       // ✅ add
                        navController.navigate(
                            ChatScreen(
                                vendorId   = vendorId,
                                vendorName = vendorName
                            )
                        )
                    },
                    viewModel   = viewModel
                )
            }
        }
        // ── Package List ──────────────────────────────


        // ── Package Detail ────────────────────────────
        // ── Package Detail ────────────────────────────────────

// ── Booking ───────────────────────────────────────────
        composable<BookingScreen> {
            // ✅ FIX: same pattern
            val pkgListEntry = runCatching {
                navController.getBackStackEntry<PackageListScreen>()
            }.getOrNull()

            val pkgViewModel: PackageListViewModel =
                if (pkgListEntry != null) hiltViewModel(pkgListEntry)
                else hiltViewModel()

            val pkg = pkgViewModel.selectedPackage
                .collectAsStateWithLifecycle().value

            val bookingViewModel = hiltViewModel<BookingViewModel>()
            val bookingState by bookingViewModel.bookingState
                .collectAsStateWithLifecycle()

            if (bookingState is AuthResult.Success) {
                bookingViewModel.resetBookingState()
                navController.navigate(BookingConfirmScreen) {
                    popUpTo<BookingScreen> { inclusive = true }
                }
            }

            pkg?.let { safePkg ->
                BookingScreen(
                    pkg           = safePkg,
                    onBack        = { navController.popBackStack() },
                    onBookingDone = {
                        navController.navigate(BookingConfirmScreen) {
                            popUpTo<BookingScreen> { inclusive = true }
                        }
                    },
                    viewModel = bookingViewModel
                )
            }
        }

        // ── Booking Confirm ───────────────────────────
        composable<BookingConfirmScreen> {

            val bookingEntry = runCatching {
                navController.getBackStackEntry<BookingScreen>()
            }.getOrNull()

            val viewModel: BookingViewModel =
                if (bookingEntry != null) {
                    hiltViewModel(bookingEntry)
                } else {
                    hiltViewModel()
                }

            val bookingState = viewModel.bookingState
                .collectAsStateWithLifecycle().value

            val booking = (bookingState as? AuthResult.Success)?.data

            if (booking != null) {
                BookingConfirmScreen(
                    booking = booking,
                    onGoHome = {
                        navController.navigate(UserDashboard) {
                            popUpTo<RegistrationScreen> { inclusive = true }
                        }
                    }
                )
            }
        }

        // ── Review ────────────────────────────────────
        composable<ReviewScreen> {
            ReviewScreen(
                bookingId   = "",       // TODO: pass actual values
                packageId   = "",
                packageName = "",
                onBack      = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() }
            )
        }


        composable<ChatScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<ChatScreen>()
            ChatScreen(
                vendorId   = route.vendorId,
                vendorName = route.vendorName,
                onBack     = { navController.popBackStack() }
            )
        }
    }
}