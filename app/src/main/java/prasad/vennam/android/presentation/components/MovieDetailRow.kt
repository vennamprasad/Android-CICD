package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import prasad.vennam.android.ui.theme.sdp
import prasad.vennam.android.ui.theme.ssp

@Composable
fun MovieDetailRow(
    label: String = "",
    value: String,
    dynamicColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.sdp),
        horizontalArrangement = Arrangement.spacedBy(10.sdp)
    ) {
        Text(
            text = "$label:", style = TextStyle(
                color = dynamicColor, fontSize = 12.ssp, fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = value, style = TextStyle(
                color = dynamicColor, fontSize = 12.ssp, fontWeight = FontWeight.Normal
            ), overflow = TextOverflow.Ellipsis
        )
    }
}