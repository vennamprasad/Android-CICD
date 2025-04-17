package prasad.vennam.android.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import prasad.vennam.android.utils.PROFILE_WIDTH
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun CommonPosterCard(id: Int, poster: String, onItemClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.clickable {
            onItemClick(id)
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SubcomposeAsyncImage(
            model = getBackgroundImageUrl(poster, size = PROFILE_WIDTH),
            contentDescription = "Profile Image",
            loading = {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Placeholder Profile",
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp)
                )
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
        )
    }
}