package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import prasad.vennam.android.data.remote.datasources.response.SearchResponse
import prasad.vennam.android.presentation.components.CommonPosterCard

@Composable
fun GenreWiseMoviesScreen(
    genreId: String,
    genreName: String,
    onMovieClick: (movieId: Int) -> Unit,
    onBackClick: () -> Boolean,
    movies: List<SearchResponse>
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            GenreWiseMoviesTopBar(
                onBackClick = onBackClick,
                genreName = genreName,
            )
        },
        content = { innerPadding ->
            GenreWiseMoviesContent(
                modifier = Modifier.padding(innerPadding),
                genreId = genreId,
                onMovieClick = onMovieClick,
                movies = movies
            )
        }
    )
}

@Composable
fun GenreWiseMoviesContent(
    modifier: Modifier,
    genreId: String,
    onMovieClick: (movieId: Int) -> Unit,
    movies: List<SearchResponse>
) {
    GenreWiseMoviesList(
        modifier = modifier, genreId = genreId, onMovieClick = onMovieClick, movies = movies
    )
}

@Composable
fun GenreWiseMoviesList(
    modifier: Modifier = Modifier,
    genreId: String,
    onMovieClick: (Int) -> Unit,
    movies: List<SearchResponse>
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = modifier.padding(16.dp),
        content = {
            items(movies.size) { index ->
                val item = movies[index]
                CommonPosterCard(
                    id = item.id ?: 0,
                    poster = item.posterPath.orEmpty(),
                    onItemClick = onMovieClick,
                    modifier = Modifier.padding(8.dp),
                    onItemClickWatchList = {

                    },
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreWiseMoviesTopBar(onBackClick: () -> Boolean, genreName: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Text(
                text = genreName,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    Icons.Default.ArrowBackIosNew, contentDescription = "Back"
                )
            }
        },
    )
}
