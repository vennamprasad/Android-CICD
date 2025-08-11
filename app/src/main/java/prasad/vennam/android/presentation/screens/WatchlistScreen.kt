package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import prasad.vennam.android.presentation.viewmodel.WatchListViewModel

@Composable
fun WatchlistScreen(
    viewModel: WatchListViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit = {},
) {
    val watchlist = viewModel.myMovieData.value.collectAsStateWithLifecycle(emptyList()).value

    WatchlistContent(
        movies = watchlist,
        onClick = onItemClick,
        onClickWatchList = { movie ->
            viewModel.addOrRemoveFromWatchList(movie)
        },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistContent(
    movies: List<MovieEntity>,
    onClick: (String) -> Unit,
    onClickWatchList: (MovieEntity) -> Unit,
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
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
        }
    ) { innerPadding ->
        if (movies.isNotEmpty()) {
            val gridState = rememberLazyGridState()

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies) { movie ->
                    CommonPosterCard(
                        id = movie.id,
                        poster = movie.posterPath,
                        onItemClick = { onClick(movie.id.toString()) },
                        onItemClickWatchList = { onClickWatchList(movie) },
                        modifier = Modifier.fillMaxWidth(),
                        isBookmarked = true
                    )
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
