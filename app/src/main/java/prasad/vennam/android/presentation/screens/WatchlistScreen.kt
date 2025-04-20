package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.presentation.components.CommonPosterCard
import prasad.vennam.android.presentation.components.MovieDetailRow
import prasad.vennam.android.presentation.viewmodel.WatchListViewModel

@Composable
fun WatchlistScreen(
    modifier: Modifier,
    viewModel: WatchListViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit = {},
) {
    val watchlist = viewModel.myMovieData.value.collectAsStateWithLifecycle(emptyList()).value
    val exist = viewModel.exist.value

    WatchlistContent(
        modifier = modifier,
        movies = watchlist,
        onClick = onItemClick,
        onClickWatchList = { movie ->
            if (exist == 0) {
                viewModel.addToWatchList(movie)
            } else {
                viewModel.removeFromWatchList(movie.id)
            }
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistContent(
    modifier: Modifier,
    movies: List<MovieEntity>,
    onClick: (String) -> Unit,
    onClickWatchList: (MovieEntity) -> Unit,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier, topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "My Watchlist",
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
            )
        }) { innerPadding ->
        if (movies.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp),
                contentPadding = PaddingValues(8.dp),
                state = rememberLazyListState(),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = rememberLazyListState())
            ) {
                items(movies) { movie ->
                    CommonPosterCard(
                        modifier = Modifier.fillMaxWidth(),
                        id = movie.id, poster = movie.backdropPath, onItemClick = {
                            onClick(movie.id.toString())
                        })
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        MovieDetailRow(
                            label = "Title",
                            value = movie.title,
                            dynamicColor = MaterialTheme.colorScheme.primary
                        )
                        MovieDetailRow(
                            label = "Rating",
                            value = movie.voteAverage.toString(),
                            dynamicColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Movies in Watchlist",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
