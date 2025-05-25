package prasad.vennam.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun TrendingMovies(
    lazyPagingItems: LazyPagingItems<TrendingMovie>,
    onMovieClick: (Int) -> Unit
) {
    Text(
        text = "Trending Movies",
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                CommonPosterCard(
                    modifier = Modifier
                        .width(140.dp)
                        .height(280.dp),
                    id = item.id,
                    poster = getBackgroundImageUrl(item.posterPath),
                    onItemClick = onMovieClick
                )
            }
        }
    }
}