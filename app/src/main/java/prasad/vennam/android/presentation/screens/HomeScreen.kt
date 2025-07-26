package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import prasad.vennam.android.presentation.components.NowPlayingMovies
import prasad.vennam.android.presentation.components.TrendingMovies
import prasad.vennam.android.presentation.components.UpComingMovies
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel
import prasad.vennam.android.utils.ListState
import prasad.vennam.android.utils.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewmodel,
    onMovieClick: (movieId: Int) -> Unit,
    onWatchlistClick: () -> Unit,
) {
    val trendingMovieListState = viewModel.trendingMovieListState.collectAsStateWithLifecycle()
    val upComingMovieViewState = viewModel.upcomingMovieListState.collectAsStateWithLifecycle()
    val nowPlayingMovieViewState = viewModel.nowPlayingMovieListState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    when (val state = trendingMovieListState.value) {
        ListState.Loading -> {
            Box {
                CircularProgressIndicator()
            }
        }

        is ListState.Success -> {
            val pagingFlow = state.pagingData
            val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        title = {
                            Text(
                                text = "TMDB-Movies",
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        actions = {
                            IconButton(onClick = { onWatchlistClick() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Note,
                                    contentDescription = "Watchlist",
                                )
                            }
                        }
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                ) {
                    TrendingMovies(
                        lazyPagingItems = lazyPagingItems,
                        onMovieClick = onMovieClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    upComingMovieViewState.value.status.let { status ->
                        when (status) {
                            Status.SUCCESS -> {
                                val upComingMovieListState = upComingMovieViewState.value.data
                                UpComingMovies(
                                    upComingMovies = upComingMovieListState!!,
                                    onItemClick = onMovieClick
                                )
                            }

                            Status.LOADING -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            Status.ERROR -> {
                                Text(text = "Error: ${upComingMovieViewState.value.message}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    nowPlayingMovieViewState.value.status.let { status ->
                        when (status) {
                            Status.SUCCESS -> {
                                val nowPlayingMovieListState = nowPlayingMovieViewState.value.data
                                NowPlayingMovies(
                                    upComingMovies = nowPlayingMovieListState!!,
                                    onItemClick = onMovieClick
                                )
                            }

                            Status.LOADING -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {

                                }
                            }

                            Status.ERROR -> {
                                Text(text = "Error: ${nowPlayingMovieViewState.value.message}")
                            }
                        }
                    }
                }
            }
        }

        is ListState.Error -> {
            Text(text = "Error: ${state.error.message}")
        }
    }
}
