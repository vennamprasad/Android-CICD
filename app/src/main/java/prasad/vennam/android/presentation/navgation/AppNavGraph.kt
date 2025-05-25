package prasad.vennam.android.presentation.navgation


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.glance.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import prasad.vennam.android.presentation.components.NoInternetScreen
import prasad.vennam.android.presentation.screens.AppSplashScreen
import prasad.vennam.android.presentation.screens.ForgotPasswordScreen
import prasad.vennam.android.presentation.screens.GenreWiseMoviesScreen
import prasad.vennam.android.presentation.screens.HomeScreen
import prasad.vennam.android.presentation.screens.LoginScreen
import prasad.vennam.android.presentation.screens.MovieDetailScreen
import prasad.vennam.android.presentation.screens.OnboardingScreen
import prasad.vennam.android.presentation.screens.SignUpScreen
import prasad.vennam.android.presentation.screens.WatchlistScreen
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel
import prasad.vennam.android.presentation.viewmodel.WatchListViewModel
import prasad.vennam.android.utils.NetworkUtils

@Composable
fun AppNavGraph(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController, startDestination = Route.Splash.route
    ) {
        composable(Route.Splash.route) {
            AppSplashScreen(
                delay = 1000L,
                onTimeout = {
                    navController.navigate(Route.Onboarding.route) {
                        popUpTo(Route.Splash.route) { inclusive = true }
                    }
                },
                modifier = modifier
            )
        }
        composable(Route.Onboarding.route) {
            OnboardingScreen(
                modifier = modifier,
                onClick = { route ->
                    when (route) {
                        Route.Login.route -> {
                            navController.navigate(Route.Login.route) {
                                popUpTo(Route.Onboarding.route) { inclusive = false }
                            }
                        }

                        Route.SignUp.route -> {
                            navController.navigate(Route.SignUp.route) {
                                popUpTo(Route.Onboarding.route) { inclusive = false }
                            }
                        }

                        Route.Home.route -> {
                            navController.navigate(Route.Home.route) {
                                popUpTo(Route.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        composable(Route.Login.route) {
            LoginScreen(
                modifier,
                onLoginSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Onboarding.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Route.ForgotPassword.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Route.SignUp.route) {
                        popUpTo(Route.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.SignUp.route) {
            SignUpScreen(
                modifier = modifier,
                onSignUpSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Onboarding.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.SignUp.route) { inclusive = true }
                    }
                },
                onExternalLink = {

                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.ForgotPassword.route) {
            ForgotPasswordScreen(
                modifier = modifier,
                onBackClick = {
                    navController.popBackStack()
                },
                onResetPasswordClick = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Home.route) {
            if (NetworkUtils.isNetworkAvailable(androidx.compose.ui.platform.LocalContext.current)) {
                val viewModel: HomeViewmodel = hiltViewModel()
                HomeScreen(
                    viewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                            popUpTo(Route.Home.route) { inclusive = false }
                        }
                    },
                    onWatchlistClick = {
                        navController.navigate(Route.Watchlist.route) {
                            popUpTo(Route.Home.route) { inclusive = false }
                        }
                    },
                )
            } else {
                NoInternetScreen()
            }
        }

        composable(
            route = "${Route.MovieDetails.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
        ) {
            if (NetworkUtils.isNetworkAvailable(androidx.compose.ui.platform.LocalContext.current)) {
                val viewModel: MovieDetailsViewmodel = hiltViewModel()
                MovieDetailScreen(
                    modifier,
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onItemClick = { movieId ->
                        navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                            popUpTo(Route.MovieDetails.route) { inclusive = false }
                        }
                    }
                )
            } else {
                NoInternetScreen()
            }
        }

        composable(
            route = Route.GenreWiseMovie.route + "/{genreId}" + "/{genreName}",
            arguments = listOf(
                navArgument("genreId") { type = NavType.StringType },
                navArgument("genreName") { type = NavType.StringType }
            )
        ) {
            GenreWiseMoviesScreen(

            )
        }

        composable(Route.Watchlist.route) {
            val viewModel: WatchListViewModel = hiltViewModel()
            WatchlistScreen(
                modifier,
                viewModel,
                onItemClick = { movieId ->
                    navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                        popUpTo(Route.Watchlist.route) { inclusive = false }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
