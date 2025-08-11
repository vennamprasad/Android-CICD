package prasad.vennam.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import prasad.vennam.android.utils.POSTER_WIDTH_VERTICAL
import prasad.vennam.android.utils.getBackgroundImageUrl


@Composable
fun CommonPosterCard(
    id: Int,
    poster: String,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onItemClickWatchList: (Int) -> Unit,
    isBookmarked: Boolean = false
) {
    Card(
        modifier = modifier
            .clickable { onItemClick(id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(2f / 3f)
        ) {
            // Poster Image
            SubcomposeAsyncImage(
                model = getBackgroundImageUrl(poster, size = POSTER_WIDTH_VERTICAL),
                contentDescription = "Poster Image",
                contentScale = ContentScale.FillBounds,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                },
                error = {
                    Icon(
                        imageVector = Icons.Filled.BrokenImage,
                        contentDescription = "Image Load Failed",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isBookmarked) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = "Add to Watchlist",
                    modifier = Modifier
                        .size(56.dp)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable {
                            onItemClickWatchList(id)
                        }
                        .padding(8.dp))
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun CommonPosterCardPreview() {
    CommonPosterCard(
        id = 1,
        poster = "https://example.com/poster.jpg",
        onItemClick = {},
        onItemClickWatchList = {},
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        isBookmarked = true
    )
}

