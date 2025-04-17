package prasad.vennam.android.presentation.components

import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.ui.theme.AppTypography

@Composable
fun CastContent(listOfCasts: List<MovieCast>) {
    Text(
        modifier = Modifier.padding(PaddingValues(horizontal = 16.dp)),
        text = "Cast",
        style = AppTypography.bodyLarge,
        fontWeight = FontWeight.Bold,
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .clipScrollableContainer(orientation = Orientation.Horizontal),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOfCasts) { movieCast ->
                CastCard(movieCast)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}