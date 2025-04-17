package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.domain.model.MovieFullDetails
import prasad.vennam.android.domain.model.NowPlayingMovie
import prasad.vennam.android.domain.model.UpcomingMovie
import prasad.vennam.android.presentation.components.CastContent
import prasad.vennam.android.presentation.components.GenreGridContent
import prasad.vennam.android.presentation.components.MoviePoster
import prasad.vennam.android.presentation.components.NowPlayingMoviesContent
import prasad.vennam.android.presentation.components.SimilarMoviesContent
import prasad.vennam.android.presentation.components.UpcomingMoviesContent
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel
import prasad.vennam.android.utils.Status

@Composable
fun MovieDetailScreen(
    modifier: Modifier,
    viewModel: MovieDetailsViewmodel,
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val movieFullDetailState by viewModel.movieFullDetailState.collectAsState()

    when (movieFullDetailState.status) {
        Status.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Status.SUCCESS -> {
            val movieDetail = movieFullDetailState.data
            if (movieDetail != null) {
                MovieDetailContent(
                    modifier = modifier,
                    movieDetail = movieDetail,
                    onBackClick = onBackClick,
                    onItemClick = onItemClick
                )
            } else {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Movie details not available")
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
    modifier: Modifier,
    movieDetail: MovieFullDetails,
    onBackClick: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            MoviePoster(
                posterPath = movieDetail.backdropPath,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movieDetail.title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "⭐ ${movieDetail.voteAverage} | ${movieDetail.originalLanguage.uppercase()} | ${movieDetail.runtime} min | ${
                    if (movieDetail.adult) "18+" else "Not Adult"
                }",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieDetail.overview, modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Displaying additional information
            Text(
                text = "Release Date: ${movieDetail.releaseDate}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tagline: ${movieDetail.tagline.orEmpty()}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Production Companies: ${movieDetail.productionCompanies}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Production Countries: ${movieDetail.productionCountries}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Spoken Languages: ${movieDetail.spokenLanguages}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            GenreGridContent(
                genres = movieDetail.genres
            )
            CastContent(
                listOfCasts = movieDetail.castList
            )
            SimilarMoviesContent(
                similarMovies = movieDetail.similarMovies,
                onItemClick
            )
            NowPlayingMoviesContent(
                nowPlayingMovies = movieDetail.nowPlayingMovies,
                onItemClick
            )
            UpcomingMoviesContent(
                upcomingMovies = movieDetail.upComingMovies,
                onItemClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        IconButton(
            onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailContentPreview() {
    Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MovieDetailContent(
            modifier = Modifier.padding(PaddingValues(16.dp)),
            movieDetail = MovieFullDetails(
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
                ),
                posterPath = "",
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
                ),
                nowPlayingMovies = listOf(
                    NowPlayingMovie(
                        id = 0,
                        poster = ""
                    ),
                    NowPlayingMovie(
                        id = 1,
                        poster = ""
                    ),
                    NowPlayingMovie(
                        id = 2,
                        poster = ""
                    )
                ),
                upComingMovies = listOf(
                    UpcomingMovie(
                        id = 0,
                        poster = ""
                    ),
                    UpcomingMovie(
                        id = 1,
                        poster = ""
                    ),
                    UpcomingMovie(
                        id = 2,
                        poster = ""
                    )
                ),
                similarMovies = emptyList(),
                releaseDate = "2023-10-10",
                runtime = 20000,
                tagline = ""
            ),
            onBackClick = {},
            onItemClick = {}
        )
    }
}








