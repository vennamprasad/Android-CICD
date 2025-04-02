package prasad.vennam.android.presentation.screens

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.palette.graphics.Palette
import coil.Coil
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.presentation.viewmodel.HomeViewmodel
import prasad.vennam.android.ui.theme.sdp
import prasad.vennam.android.ui.theme.ssp
import prasad.vennam.android.utils.ListState
import prasad.vennam.android.utils.POSTER_WIDTH
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewmodel,
    onMovieClick: (movieId: Int) -> Unit,
) {
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
            LazyColumn(
                modifier = modifier
                    .clipToBounds()
                    .clipScrollableContainer(orientation = Orientation.Vertical)
                    .fillMaxWidth(),
            ) {
                items(lazyPagingItems.itemCount) { index ->
                    val item = lazyPagingItems[index]
                    if (item != null) {
                        MovieRow(item, onMovieClick)
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
fun MovieRow(
    movie: TrendingMovie,
    onMovieClick: (movieId: Int) -> Unit,
    scrollState: LazyListState? = null,
) {
    val imageUrl = getBackgroundImageUrl(movie.posterPath, POSTER_WIDTH)
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val dynamicBackgroundColor = remember { mutableStateOf(Color.Gray) }
    val dynamicTextColor = remember { mutableStateOf(Color.White) }
    val context = LocalContext.current
    var isImageLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(imageUrl) {
        val result = Coil.imageLoader(context).execute(
            ImageRequest.Builder(context).data(imageUrl).allowHardware(false).build()
        )

        (result.drawable as? BitmapDrawable)?.bitmap?.let { bmp ->
            bitmap.value = bmp
            Palette.from(bmp).generate { palette ->
                dynamicBackgroundColor.value =
                    Color(palette?.getLightMutedColor(Color.Gray.toArgb()) ?: Color.Gray.toArgb())
                dynamicTextColor.value =
                    Color(palette?.getDarkMutedColor(Color.White.toArgb()) ?: Color.White.toArgb())
            }
        }
    }

    val scrollOffset by remember {
        derivedStateOf { scrollState?.firstVisibleItemScrollOffset?.div(10f) ?: 0f }
    }

    Row(
        modifier = Modifier
            .clickable(enabled = true) {
                onMovieClick(movie.id)
            }
            .background(color = dynamicBackgroundColor.value)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Animated Image with Shimmer + Fade-In + Scale Effect
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Movie Poster", loading = {
                ShimmerEffect() // Show shimmer before image loads
            }, onSuccess = { isImageLoaded = true },
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .wrapContentHeight()
                .graphicsLayer {
                    alpha = if (isImageLoaded) 1f else 0f
                    scaleX = if (isImageLoaded) 1f else 0.9f
                    scaleY = if (isImageLoaded) 1f else 0.9f
                    translationY = scrollOffset
                }
                .animateContentSize(animationSpec = tween(500)),
            contentScale = ContentScale.Crop
        )

        // Movie Details
        Column(
            modifier = Modifier
                .weight(0.6f)
                .padding(PaddingValues(8.sdp))
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            MovieDetailRow("Title", movie.title, dynamicTextColor)
            MovieDetailRow("Rating", movie.voteAverage.toString(), dynamicTextColor)
            MovieDetailRow("Language", movie.originalLanguage, dynamicTextColor)
        }
    }
}

@Composable
fun MovieDetailRow(label: String, value: String, dynamicColor: MutableState<Color>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.sdp),
        horizontalArrangement = Arrangement.spacedBy(10.sdp)
    ) {
        Text(
            text = "$label:", style = TextStyle(
                color = dynamicColor.value, fontSize = 12.ssp, fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value, style = TextStyle(
                color = dynamicColor.value, fontSize = 12.ssp, fontWeight = FontWeight.Normal
            ), overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun ShimmerEffect() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha: Float by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = alpha))
    )
}
