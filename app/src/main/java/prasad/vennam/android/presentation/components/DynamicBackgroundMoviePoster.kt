package prasad.vennam.android.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.Coil
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import prasad.vennam.android.utils.BACKDROP_WIDTH
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun DynamicBackgroundMoviePoster(
    movieId: Int,
    posterPath: String,
    onMovieClick: (movieId: Int) -> Unit,
) {
    val imageUrl = getBackgroundImageUrl(posterPath, BACKDROP_WIDTH)
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
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Movie Poster", loading = {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            },
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
    }
}