package prasad.vennam.android.presentation.navgation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import prasad.vennam.android.presentation.screens.AppSplashScreen
import prasad.vennam.android.presentation.screens.HomeScreen
import prasad.vennam.android.presentation.screens.LoginScreen
import prasad.vennam.android.presentation.screens.OnboardingScreen
import prasad.vennam.android.presentation.screens.SignUpScreen
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController, startDestination = Route.Splash.route
    ) {
        composable(Route.Splash.route) {
            AppSplashScreen(
                delay = 5000L, onTimeout = {
                    navController.navigate(Route.Onboarding.route) {
                        popUpTo(Route.Splash.route) { inclusive = true }
                    }
                })
        }
        composable(Route.Onboarding.route) {
            OnboardingScreen(
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
                        popUpTo<Route.Login> {
                            inclusive = true
                        }
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

                }
            )
        }
        composable(Route.Home.route) {
            // viewmodel
            val viewModel: HomeViewmodel = hiltViewModel()
            HomeScreen(
                viewModel
            )
        }
    }
}
