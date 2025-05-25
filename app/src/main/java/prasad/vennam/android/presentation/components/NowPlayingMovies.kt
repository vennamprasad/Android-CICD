package prasad.vennam.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun NowPlayingMovies(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit,
) {
    NowPlayingMoviesContent(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick
    )
}

@Composable
fun NowPlayingMoviesContent(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit
) {
    NowPlayingMoviesList(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick
    )
}

@Composable
fun NowPlayingMoviesList(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit
) {
    Text(
        text = "NowPlaying Movies",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        items(upComingMovies.size) { index ->
            val item = upComingMovies[index]
            CommonPosterCard(
                id = item.id,
                poster = getBackgroundImageUrl(item.poster),
                onItemClick = onItemClick,
                modifier = Modifier.size(200.dp, 300.dp),
                onItemClickWatchList = {

                }
            )
        }
    }
}
