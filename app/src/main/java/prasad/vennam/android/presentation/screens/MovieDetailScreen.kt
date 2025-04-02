package prasad.vennam.android.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.domain.model.MovieFullDetails
import prasad.vennam.android.presentation.viewmodel.MovieDetailsViewmodel
import prasad.vennam.android.ui.theme.AppTypography
import prasad.vennam.android.utils.PROFILE_WIDTH
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier, viewModel: MovieDetailsViewmodel, onBackClick: () -> Unit
) {
    val movieFullDetailState by viewModel.movieFullDetailState.collectAsState()

    when (movieFullDetailState.status) {
        Status.LOADING -> {
            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                    onBackClick = onBackClick
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
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SubcomposeAsyncImage(
                model = getBackgroundImageUrl(movieDetail.posterPath),
                contentDescription = null,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movieDetail.title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "⭐ ${movieDetail.voteAverage} | ${movieDetail.originalLanguage.uppercase()}",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movieDetail.overview, modifier = Modifier.padding(horizontal = 16.dp)
            )

            GenreGridScreen(
                genres = movieDetail.genres
            )
            CastScreen(
                listOfCasts = movieDetail.castList
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            IconButton(
                onClick = onBackClick, modifier = modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun GenreGridScreen(genres: List<Genre>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Genres",
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .clipScrollableContainer(orientation = Orientation.Horizontal),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(genres) { genre ->
                GenreChip(genre)
            }
        }
    }
}

@Composable
fun GenreChip(genre: Genre) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Text(
            text = genre.name,
            style = AppTypography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CastScreen(listOfCasts: List<MovieCast>) {
    Text(
        modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
        text = "Cast",
        style = AppTypography.bodyLarge,
        fontWeight = FontWeight.Bold,
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .clipScrollableContainer(orientation = Orientation.Horizontal),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOfCasts) { movieCast ->
                CastCard(movieCast)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CastCard(movieCast: MovieCast) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubcomposeAsyncImage(
                model = getBackgroundImageUrl(movieCast.profilePath, size = PROFILE_WIDTH),
                contentDescription = "Profile Image",
                loading = {
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Placeholder Profile",
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp)
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieCast.name,
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = movieCast.character,
                style = AppTypography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}




