package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.ui.theme.AppTypography

@Composable
fun CastContent(
    listOfCasts: List<MovieCast>,
    onCastItemClick: (Int) -> Unit
) {
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
        ) {
            items(listOfCasts.size) { index ->
                val movieCast = listOfCasts[index]
                CastCard(movieCast)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}