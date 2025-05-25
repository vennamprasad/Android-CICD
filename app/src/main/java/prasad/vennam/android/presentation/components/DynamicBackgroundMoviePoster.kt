package prasad.vennam.android.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.Coil
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import prasad.vennam.android.utils.POSTER_WIDTH_HORIZONTAL
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun DynamicBackgroundMoviePoster(
    movieId: Int,
    posterPath: String,
    onMovieClick: (movieId: Int) -> Unit,
) {
    val imageUrl = getBackgroundImageUrl(posterPath, POSTER_WIDTH_HORIZONTAL)
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val dynamicBackgroundColor = remember { mutableStateOf(Color.Gray) }
    val dynamicTextColor = remember { mutableStateOf(Color.White) }
    val context = LocalContext.current

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

    Column(
        modifier = Modifier
            .clickable(enabled = true) {
                onMovieClick(movieId)
            }
    ) {
        // Animated Image with Shimmer + Fade-In + Scale Effect
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Movie Poster", loading = {
                Box(
                    modifier = Modifier.size(52.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
    }
}