package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.presentation.components.LoadingIndicator
import prasad.vennam.android.presentation.components.NowPlayingMovies
import prasad.vennam.android.presentation.components.SectionHeader
import prasad.vennam.android.presentation.components.TrendingMovies
import prasad.vennam.android.presentation.components.UpComingMovies
import prasad.vennam.android.presentation.viewmodel.HomeViewModel
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (movieId: Int) -> Unit,
    onWatchlistClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()


    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (isRefreshing) {
            delay(500) // Simulate network delay
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = modifier, topBar = {
            HomeTopAppBar(
                onWatchlistClick = onWatchlistClick, onRefreshClick = {
                    if (!isRefreshing) {
                        isRefreshing = true
                        viewModel.refreshAllData()
                    }
                },
                isRefreshing = isRefreshing,
                onSearchClick = onSearchClick
            )

        }) { paddingValues ->
        HomeContent(
            paddingValues,
            scrollState,
            isRefreshing,
            uiState,
            onMovieClick,
            viewModel
        )
    }
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
    scrollState: ScrollState,
    isRefreshing: Boolean,
    uiState: HomeViewModel.HomeUiState,
    onMovieClick: (Int) -> Unit,
    viewModel: HomeViewModel,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if (isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), strokeWidth = 2.dp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TrendingMoviesSection(
            uiState.trendingMovies, onMovieClick = onMovieClick
        ) {
            viewModel.refreshTrendingMovies()
        }

        Spacer(modifier = Modifier.height(24.dp))

        UpcomingMoviesSection(
            upcomingState = uiState.upcomingMovies,
            onMovieClick = onMovieClick,
            onRetry = { viewModel.refreshUpcomingMovies() })

        Spacer(modifier = Modifier.height(24.dp))

        NowPlayingMoviesSection(
            nowPlayingState = uiState.nowPlayingMovies,
            onMovieClick = onMovieClick,
            onBookmarkClick = { movieId ->

            },
            onRetry = { viewModel.refreshNowPlayingMovies() })

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onWatchlistClick: () -> Unit,
    onRefreshClick: () -> Unit,
    isRefreshing: Boolean = false,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ), title = {
            Text(
                text = "TMDB Movies",
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search"
                )
            }
            IconButton(
                onClick = onRefreshClick, enabled = !isRefreshing
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp), strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh, contentDescription = "Refresh"
                    )
                }
            }
            IconButton(onClick = onWatchlistClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Watchlist",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
}


@Composable
private fun TrendingMoviesSection(
    trendingMoviesPagingItems: ViewState<Flow<PagingData<TrendingMovie>>>,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit
) {
    when (trendingMoviesPagingItems.status) {
        Status.SUCCESS -> {
            val lazyPagingItems = trendingMoviesPagingItems.data?.collectAsLazyPagingItems()
            lazyPagingItems?.let {
                SectionHeader(title = "Trending Movies")
                TrendingMovies(
                    lazyPagingItems = it,
                    onMovieClick = onMovieClick
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

        Status.ERROR -> {
            ErrorMessage(
                message = trendingMoviesPagingItems.message ?: "Failed to load trending movies",
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun UpcomingMoviesSection(
    upcomingState: ViewState<List<CommonMovie>>,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit
) {
    when (upcomingState.status) {
        Status.SUCCESS -> {
            val movies = upcomingState.data ?: emptyList()
            if (movies.isEmpty()) {
                EmptyState(message = "No upcoming movies available")
            } else {
                SectionHeader(title = "Upcoming Movies")
                UpComingMovies(
                    upComingMovies = movies, onItemClick = onMovieClick
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

        Status.ERROR -> {
            ErrorMessage(
                message = upcomingState.message ?: "Failed to load upcoming movies",
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun NowPlayingMoviesSection(
    nowPlayingState: ViewState<List<prasad.vennam.android.domain.model.CommonMovie>>,
    onMovieClick: (Int) -> Unit,
    onBookmarkClick: (Int) -> Unit,
    onRetry: () -> Unit
) {
    when (nowPlayingState.status) {
        Status.SUCCESS -> {
            val movies = nowPlayingState.data ?: emptyList()
            if (movies.isEmpty()) {
                EmptyState(message = "No movies currently playing")
            } else {
                SectionHeader(title = "Now Playing")
                NowPlayingMovies(
                    upComingMovies = movies,
                    onItemClick = onMovieClick,
                    onBookMarkClick = onBookmarkClick
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

        Status.ERROR -> {
            ErrorMessage(
                message = nowPlayingState.message ?: "Failed to load now playing movies",
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyState(
    message: String, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}