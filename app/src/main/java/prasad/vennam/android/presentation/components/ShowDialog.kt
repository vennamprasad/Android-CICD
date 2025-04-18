package prasad.vennam.android.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissButtonText: String = "Cancel",
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = dismissButtonText)
            }
        })
}

@Composable
@Preview
fun ShowDialogPreview() {
    ShowDialog(
        modifier = Modifier,
        title = "Exit",
        message = "Are you sure you want to exit?",
        confirmButtonText = "Yes",
        onConfirm = {},
        onDismiss = {}
    )
}
