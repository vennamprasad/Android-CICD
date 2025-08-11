package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.automirrored.outlined.Note
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.domain.model.MovieFullDetails
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.presentation.components.CastContent
import prasad.vennam.android.presentation.components.GenreGridContent
import prasad.vennam.android.presentation.components.MoviePoster
import prasad.vennam.android.presentation.components.SimilarMoviesContent
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel
import prasad.vennam.android.utils.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewmodel,
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onGenreItemClick: (Genre) -> Unit,
    onCastItemClick: (Int) -> Unit,
) {
    val movieFullDetailState by viewModel.movieFullDetailState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    when (movieFullDetailState.status) {
        Status.LOADING -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        Status.SUCCESS -> {
            val movieDetails = movieFullDetailState.data

            if (movieDetails != null) {
                Scaffold(
                    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        MediumTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                scrolledContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            title = {
                                Column {
                                    Text(
                                        text = movieDetails.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    when {
                                        movieDetails.tagline?.isNotEmpty() == true -> {
                                            Text(
                                                text = movieDetails.tagline,
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        else -> {
                                            Text(
                                                text = "⭐ ${movieDetails.voteAverage} | ${movieDetails.originalLanguage.uppercase()} | ${movieDetails.runtime} min | ${
                                                    if (movieDetails.adult) "18+" else "Not Adult"
                                                }",
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = onBackClick) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    viewModel.toggleMovieSavedStatus(movieDetails.id)
                                }) {
                                    Icon(
                                        imageVector = if (movieDetails.isSaved) Icons.Filled.Favorite
                                        else Icons.Filled.FavoriteBorder,
                                        contentDescription = null
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                        )
                    }) { paddingValues ->
                    val lazyListState = rememberLazyListState()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        state = lazyListState
                    ) {
                        item {
                            MovieDetailContent(
                                movieDetails = movieDetails,
                                onItemClick = onItemClick,
                                onCastItemClick = onCastItemClick,
                                onGenreItemClick = onGenreItemClick
                            )
                        }
                    }
                }
            } else {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Movie details not available")
                }
            }
        }

        Status.ERROR -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = movieFullDetailState.message ?: "Error occurred")
            }
        }
    }
}


@Composable
fun MovieDetailContent(
    movieDetails: MovieFullDetails,
    onItemClick: (Int) -> Unit,
    onCastItemClick: (Int) -> Unit,
    onGenreItemClick: (Genre) -> Unit,
) {
    Column {
        MoviePoster(
            modifier = Modifier.fillMaxWidth(),
            posterPath = movieDetails.backdropPath
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movieDetails.overview, modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Release Date: ")
                }
                append(movieDetails.releaseDate)
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GenreGridContent(
            genres = movieDetails.genres,
            onGenreItemClick
        )
        CastContent(
            listOfCasts = movieDetails.castList,
            onCastItemClick = onCastItemClick
        )
        SimilarMoviesContent(
            similarMovies = movieDetails.similarMovies, onItemClick
        )
    }
}

@Preview(
    name = "MovieDetailContent",
    showSystemUi = true,
    device = Devices.PIXEL_7A,
)
@Composable
fun MovieDetailContentPreview() {
    Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MovieDetailContent(
            movieDetails = MovieFullDetails(
                id = 111222,
                title = "Times of Telangana",
                voteAverage = 9.9,
                originalLanguage = "Telugu",
                backdropPath = "",
                overview = "lorem",
                genres = listOf(
                    Genre(0, "Thriller"),
                    Genre(1, "Horror"),
                    Genre(2, "Romance")
                ), posterPath = "www.example.com/poster.jpg",
                castList = listOf(
                    MovieCast(
                        id = 0,
                        name = "Prasad",
                        character = "Prasad",
                        gender = 0,
                    ),
                    MovieCast(
                        id = 0,
                        name = "Vennam",
                        character = "Vennam",
                        gender = 0,
                    ),
                    MovieCast(
                        id = 0,
                        name = "Vennam",
                        character = "Vennam",
                        gender = 0,
                    )
                ), similarMovies = listOf(
                    TrendingMovie(
                        id = 1,
                        posterPath = "https://image.tmdb.org/t/p/w500/abc123.jpg",
                        title = "Movie 1",
                        overview = "Overview of Movie 1",
                        voteAverage = 8.5,
                        backdropPath = "https://image.tmdb.org/t/p/w500/def456.jpg",
                        originalLanguage = "en",
                        isSaved = false,
                    ),
                ),
                releaseDate = "2023-10-10",
                runtime = 20000,
                tagline = ""
            ), onItemClick = {}, onCastItemClick = {}, onGenreItemClick = {}
        )
    }
}








