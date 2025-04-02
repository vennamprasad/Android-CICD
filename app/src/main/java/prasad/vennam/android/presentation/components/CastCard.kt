package prasad.vennam.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.ui.theme.AppTypography
import prasad.vennam.android.utils.PROFILE_WIDTH
import prasad.vennam.android.utils.getBackgroundImageUrl

@Composable
fun CastCard(movieCast: MovieCast) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubcomposeAsyncImage(
                model = getBackgroundImageUrl(movieCast.profilePath, size = PROFILE_WIDTH),
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieCast.name,
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = movieCast.character,
                style = AppTypography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}