package prasad.vennam.android.presentation.navgation


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import prasad.vennam.android.presentation.screens.AppSplashScreen
import prasad.vennam.android.presentation.screens.ForgotPasswordScreen
import prasad.vennam.android.presentation.screens.HomeScreen
import prasad.vennam.android.presentation.screens.LoginScreen
import prasad.vennam.android.presentation.screens.MovieDetailScreen
import prasad.vennam.android.presentation.screens.OnboardingScreen
import prasad.vennam.android.presentation.screens.SignUpScreen
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {
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
            val viewModel: HomeViewmodel = hiltViewModel()
            HomeScreen(
                modifier,
                viewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                        popUpTo(Route.Home.route) { inclusive = false }
                    }
                },
            )
        }

        composable(
            route = "${Route.MovieDetails.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
        ) {
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
        }
    }
}
