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
fun UpComingMovies(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit,
) {
    UpComingMoviesContent(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick
    )
}

@Composable
fun UpComingMoviesContent(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit
) {
    UpComingMoviesList(
        upComingMovies = upComingMovies,
        onItemClick = onItemClick
    )
}

@Composable
fun UpComingMoviesList(
    upComingMovies: List<CommonMovie>,
    onItemClick: (Int) -> Unit
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

                },
                isBookmarked = item.isBookmarked
            )
        }
    }
}
