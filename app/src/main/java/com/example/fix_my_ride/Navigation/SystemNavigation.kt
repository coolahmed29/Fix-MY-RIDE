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
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.view.MechanicMapScreen
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.view.MechanicListScreen
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.view.MechanicProfileScreen
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.view.ServiceRequestScreen
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.view.LiveTrackingScreen
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel.MechanicFinderViewModel
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic

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

        // ── Dashboard ─────────────────────────────────
        composable<UserDashboard> {
            DashboardScreen(
                onNavigateToSpareParts = {
                    navController.navigate(SparePartsScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToDetailing = {
                    navController.navigate(PackageListScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                // ✅ ADD THIS - Mechanic Finder
                onNavigateToMechanicFinder = {
                    navController.navigate(MechanicMapScreenRoute) {
                        launchSingleTop = true
                        restoreState = true
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
                    navController.navigate(UserDashboard) {
                        popUpTo<SparePartsScreen> { inclusive = true }
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
                    onChatClick = { vendorId, vendorName ->
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
        composable<PackageListScreen> {
            val viewModel = hiltViewModel<PackageListViewModel>()
            PackageListScreen(
                onPackageClick = { pkg ->
                    viewModel.onPackageSelect(pkg)
                    navController.navigate(PackageDetailScreen)
                },
                onBack = {
                    navController.navigate(UserDashboard) {
                        popUpTo<PackageListScreen> { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        // ── Package Detail ────────────────────────────
        composable<PackageDetailScreen> {
            val pkgListEntry = runCatching {
                navController.getBackStackEntry<PackageListScreen>()
            }.getOrNull()

            val viewModel: PackageListViewModel =
                if (pkgListEntry != null) hiltViewModel(pkgListEntry)
                else hiltViewModel()

            val pkg = viewModel.selectedPackage
                .collectAsStateWithLifecycle().value

            pkg?.let { safePkg ->
                PackageDetailScreen(
                    pkg       = safePkg,
                    onBack    = { navController.popBackStack() },
                    onBookNow = { navController.navigate(BookingScreen) }
                )
            }
        }

        // ── Booking ───────────────────────────────────
        composable<BookingScreen> {
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
                bookingId   = "",
                packageId   = "",
                packageName = "",
                onBack      = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() }
            )
        }

        // ── Chat ──────────────────────────────────────
        composable<ChatScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<ChatScreen>()
            ChatScreen(
                vendorId   = route.vendorId,
                vendorName = route.vendorName,
                onBack     = { navController.popBackStack() }
            )
        }

        // ════════════════════════════════════════════════════════════
        // ── MECHANIC FINDER ROUTES ────────────────────────────────
        // ════════════════════════════════════════════════════════════

        // ── Mechanic Map Screen ───────────────────────
        composable<MechanicMapScreenRoute> {
            val viewModel = hiltViewModel<MechanicFinderViewModel>()
            MechanicMapScreen(
                onMechanicClick = { mechanicId, mechanicName ->
                    navController.navigate(MechanicProfileScreenRoute(mechanicId = mechanicId))
                },
                onBack = {
                    navController.navigate(UserDashboard) {
                        popUpTo<MechanicMapScreenRoute> { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        // ── Mechanic List Screen ──────────────────────
        composable<MechanicListScreenRoute> {
            val viewModel = hiltViewModel<MechanicFinderViewModel>()
            MechanicListScreen(
                onMechanicClick = { mechanicId, mechanicName ->
                    navController.navigate(MechanicProfileScreenRoute(mechanicId = mechanicId))
                },
                onBack = {
                    navController.navigate(UserDashboard) {
                        popUpTo<MechanicListScreenRoute> { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }

        // ── Mechanic Profile Screen ───────────────────
        composable<MechanicProfileScreenRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<MechanicProfileScreenRoute>()
            val viewModel = hiltViewModel<MechanicFinderViewModel>()
            val mechanicProfileState by viewModel.mechanicProfileState
                .collectAsStateWithLifecycle()

            val mechanic = (mechanicProfileState as? AuthResult.Success<*>)?.data
                    as? com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic

            MechanicProfileScreen(
                mechanic = mechanic,
                onBack = { navController.popBackStack() },
                onServiceRequest = {
                    navController.navigate(
                        ServiceRequestScreenRoute(mechanicId = route.mechanicId)
                    )
                },
                onChatClick = { vendorId, vendorName ->
                    navController.navigate(
                        ChatScreen(
                            vendorId   = vendorId,
                            vendorName = vendorName
                        )
                    )
                },
                viewModel = viewModel
            )
        }

        // ── Service Request Screen ────────────────────
        composable<ServiceRequestScreenRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ServiceRequestScreenRoute>()
            val viewModel = hiltViewModel<MechanicFinderViewModel>()
            val mechanicProfileState by viewModel.mechanicProfileState
                .collectAsStateWithLifecycle()

            val mechanic = (mechanicProfileState as? AuthResult.Success<*>)?.data
                    as? com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic

            if (mechanic != null) {
                ServiceRequestScreen(
                    mechanic = mechanic ,
                    userId = "user_id_placeholder",  // TODO: Get from auth
                    userLatitude = 24.8607,           // TODO: Get from location
                    userLongitude = 67.0011,          // TODO: Get from location
                    onBack = { navController.popBackStack() },
                    onSuccess = {
                        navController.navigate(LiveTrackingScreenRoute(requestId = "request_id_placeholder")) {
                            popUpTo<ServiceRequestScreenRoute> { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }
        }

        // ── Live Tracking Screen ──────────────────────
        composable<LiveTrackingScreenRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LiveTrackingScreenRoute>()
            val viewModel = hiltViewModel<MechanicFinderViewModel>()

            val mechanicProfileState by viewModel.mechanicProfileState
                .collectAsStateWithLifecycle()
            val serviceRequestState by viewModel.serviceRequestState
                .collectAsStateWithLifecycle()

            val mechanic = (mechanicProfileState as? AuthResult.Success<*>)?.data
                    as? com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic

            val serviceRequest = (serviceRequestState as? AuthResult.Success<*>)?.data
                    as? com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest

            LiveTrackingScreen(
                serviceRequest = serviceRequest,
                mechanic = mechanic,
                onBack = {
                    navController.navigate(UserDashboard) {
                        popUpTo<LiveTrackingScreenRoute> { inclusive = true }
                    }
                },
                onChatClick = { vendorId, vendorName ->
                    navController.navigate(
                        ChatScreen(
                            vendorId   = vendorId,
                            vendorName = vendorName
                        )
                    )
                },
                viewModel = viewModel
            )
        }
    }
}