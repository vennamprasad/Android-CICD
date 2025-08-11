package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.padding
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
import prasad.vennam.android.data.remote.datasources.response.MovieResponse

@Composable
fun GenreWiseMoviesScreen(
    genreId: String,
    genreName: String,
    onMovieClick: (movieId: Int) -> Unit,
    onBackClick: () -> Boolean,
    movies: List<MovieResponse>
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
                onMovieClick = onMovieClick
            )
        }
    )
}

@Composable
fun GenreWiseMoviesContent(
    modifier: Modifier,
    genreId: String,
    onMovieClick: (movieId: Int) -> Unit
) {

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
