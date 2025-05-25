package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.ui.theme.AppTypography
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun SimilarMoviesContent(
    similarMovies: List<TrendingMovie>,
    onItemClick: (Int) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
            text = "Similar Movies",
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
        ) {
            items(similarMovies.size) { index ->
                val item = similarMovies[index]
                CommonPosterCard(
                    id = item.id,
                    poster = getBackgroundImageUrl(item.posterPath),
                    onItemClick = onItemClick,
                    modifier = Modifier.size(200.dp, 300.dp),
                    onItemClickWatchList = {

                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun SimilarMoviesContentPreview() {
    SimilarMoviesContent(
        similarMovies = listOf(
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
        ), onItemClick = {})
}