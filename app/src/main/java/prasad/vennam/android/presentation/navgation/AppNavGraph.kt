package prasad.vennam.android.presentation.navgation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import prasad.vennam.android.presentation.components.LoadingIndicator
import prasad.vennam.android.presentation.screens.AppSplashScreen
import prasad.vennam.android.presentation.screens.ForgotPasswordScreen
import prasad.vennam.android.presentation.screens.GenreWiseMoviesScreen
import prasad.vennam.android.presentation.screens.HomeScreen
import prasad.vennam.android.presentation.screens.LoginScreen
import prasad.vennam.android.presentation.screens.MovieDetailScreen
import prasad.vennam.android.presentation.screens.MovieSearchScreen
import prasad.vennam.android.presentation.screens.NetworkAwareScreen
import prasad.vennam.android.presentation.screens.OnboardingScreen
import prasad.vennam.android.presentation.screens.SignUpScreen
import prasad.vennam.android.presentation.screens.WatchlistScreen
import prasad.vennam.android.presentation.viewmodel.GenreWiseMoviesViewModel
import prasad.vennam.android.presentation.viewmodel.HomeViewModel
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel
import prasad.vennam.android.presentation.viewmodel.MovieSearchViewModel
import prasad.vennam.android.presentation.viewmodel.WatchListViewModel
import prasad.vennam.android.utils.Status

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
                    navController.navigate(Route.Onboarding.route) {
                        popUpTo(Route.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.ForgotPassword.route) { inclusive = true }
                    }
                },
                onResetPasswordClick = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(Route.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Home.route) {
            NetworkAwareScreen {
                val viewModel: HomeViewModel = hiltViewModel()
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
                    onSearchClick = { navController.navigate("search") }
                )
            }
        }

        composable(
            route = "${Route.MovieDetails.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
        ) {
            NetworkAwareScreen {
                val viewModel: MovieDetailsViewmodel = hiltViewModel()
                MovieDetailScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onItemClick = { movieId ->
                        navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                            popUpTo(Route.MovieDetails.route) { inclusive = false }
                        }
                    },
                    onGenreItemClick = { genre ->
                        navController.navigate(
                            "${Route.GenreWiseMovie.route}/${genre.id}/${genre.name}"
                        ) {
                            popUpTo(Route.MovieDetails.route) { inclusive = false }
                        }
                    },
                    onCastItemClick = {

                    }
                )
            }
        }

        composable(
            route = "${Route.GenreWiseMovie.route}/{genreId}/{genreName}",
            arguments = listOf(
                navArgument("genreId") { type = NavType.StringType },
                navArgument("genreName") { type = NavType.StringType }
            )
        ) {
            NetworkAwareScreen {
                val genreId = it.arguments?.getString("genreId") ?: ""
                val genreName = it.arguments?.getString("genreName") ?: "Genre"
                val viewModel: GenreWiseMoviesViewModel = hiltViewModel()
                viewModel.fetchGenreWiseMovies(genreId)
                val trendingMovieListState = viewModel.genreWiseMovies.collectAsStateWithLifecycle()
                when (trendingMovieListState.value.status) {
                    Status.SUCCESS -> {
                        GenreWiseMoviesScreen(
                            genreId = genreId,
                            genreName = genreName,
                            onMovieClick = { movieId ->
                                navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                                    popUpTo(Route.GenreWiseMovie.route) { inclusive = false }
                                }
                            },
                            onBackClick = {
                                navController.popBackStack()
                            },
                            movies = trendingMovieListState.value.data ?: emptyList(),
                        )
                    }

                    Status.ERROR -> {
                        Box {
                            Text(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                text = trendingMovieListState.value.message
                                    ?: "Error loading movies",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Status.LOADING -> {
                        LoadingIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }
        }

        composable(Route.Watchlist.route) {
            val viewModel: WatchListViewModel = hiltViewModel()
            WatchlistScreen(
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

        composable(Route.Search.route) { backStackEntry ->
            NetworkAwareScreen {
                val searchViewModel: MovieSearchViewModel =
                    hiltViewModel<MovieSearchViewModel>(backStackEntry)
                MovieSearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Route.MovieDetails.route + "/${movieId}") {
                            popUpTo(Route.Search.route) { inclusive = false }
                        }
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
