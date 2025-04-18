package prasad.vennam.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyAppBar(title: String, onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBackIosNew, contentDescription = "Back"
                ) // Use Material Icons
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
        }
    }
}

@Composable
@Preview
fun MyAppBarPreview() {
    Surface {
        MyAppBar(title = "My App Bar", onBackClick = {})
    }
}