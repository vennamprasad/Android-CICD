package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import prasad.vennam.android.BuildConfig
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel
import prasad.vennam.android.ui.theme.sdp
import prasad.vennam.android.ui.theme.ssp
import prasad.vennam.android.utils.ListState

@Composable
fun HomeScreen(viewModel: HomeViewmodel) {
    val trendingMovieListState = viewModel.trendingMovieListState.collectAsStateWithLifecycle()

    when (val state = trendingMovieListState.value) {
        ListState.Loading -> {
            Box {
                CircularProgressIndicator()
            }
        }

        is ListState.Success -> {
            val pagingFlow = state.pagingData
            val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
            println("LazyPagingItems: ${lazyPagingItems.itemCount}")
            LazyColumn {
                items(lazyPagingItems.itemCount) { index ->
                    val item = lazyPagingItems[index]
                    if (item != null) {
                        MovieRow(item)
                    }
                }
            }
        }

        is ListState.Error -> {
            Text(text = "Error: ${state.error.message}")
        }
    }
}

@Composable
fun MovieRow(movie: TrendingMovie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(BuildConfig.IMAGE_BASE_URL + movie.posterPath),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .weight(0.50f) // 40% width
                .height(150.sdp)
                .clip(RoundedCornerShape(4.sdp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.sdp))

        Column(
            modifier = Modifier.weight(0.50f)
        ) {
            MovieDetailRow("Title", movie.title)
            MovieDetailRow("Rating", movie.voteAverage.toString())
            MovieDetailRow("Language", movie.originalLanguage)
            MovieDetailRow("Overview", movie.overview.take(100))
        }
    }
}

@Composable
fun MovieDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.sdp),
        horizontalArrangement = Arrangement.spacedBy(10.sdp)
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Bold, fontSize = 14.ssp)
        Text(text = value, fontSize = 14.ssp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

