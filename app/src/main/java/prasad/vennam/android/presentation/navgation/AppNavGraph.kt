package prasad.vennam.android.presentation.navgation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import prasad.vennam.android.presentation.screens.AppSplashScreen
import prasad.vennam.android.presentation.screens.OnboardingScreen
import prasad.vennam.android.ui.theme.primaryDark
import prasad.vennam.android.ui.theme.primaryLight
import prasad.vennam.android.ui.theme.sdp

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
        composable(Route.Home.route) { }
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    onExternalLink: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryLight,
                            primaryDark
                        )
                    )
                ),
        ) {
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.sdp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { androidx.compose.material3.Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { androidx.compose.material3.Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
        )

        androidx.compose.material3.Button(
            onClick = {
                onLoginSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            androidx.compose.material3.Text("Login")
        }
    }
}
