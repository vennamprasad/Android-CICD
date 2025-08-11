package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun NowPlayingMovies(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit,
    onBookMarkClick: (Int) -> Unit
) {
    NowPlayingMoviesContent(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick,
        onBookMarkClick = {
            onBookMarkClick(it)
        }
    )
}

@Composable
fun NowPlayingMoviesContent(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit,
    onBookMarkClick: (Int) -> Unit
) {
    NowPlayingMoviesList(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick,
        onBookMarkClick = {
            onBookMarkClick(it)
        }
    )
}

@Composable
fun NowPlayingMoviesList(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit,
    onBookMarkClick: (Int) -> Unit
) {
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
                    onBookMarkClick(it)
                },
                isBookmarked = item.isBookmarked
            )
        }
    }
}
