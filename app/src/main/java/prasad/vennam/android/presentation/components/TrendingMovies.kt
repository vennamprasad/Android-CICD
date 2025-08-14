package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun TrendingMovies(
    lazyPagingItems: LazyPagingItems<TrendingMovie>,
    onMovieClick: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                CommonPosterCard(
                    id = item.id,
                    poster = getBackgroundImageUrl(item.posterPath),
                    onItemClick = onMovieClick,
                    modifier = Modifier.size(200.dp, 300.dp),
                    onItemClickWatchList = {

                    },
                )
            }
        }
    }
}