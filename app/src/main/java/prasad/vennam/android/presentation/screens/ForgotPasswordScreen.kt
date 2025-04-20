package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import prasad.vennam.android.ui.theme.sdp

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.sdp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .padding(8.sdp)
            )

            ElevatedButton(
                onClick = {
                    onResetPasswordClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(
                        42.sdp
                    ),
                shape = RoundedCornerShape(
                    topStart = 8.sdp, topEnd = 8.sdp, bottomStart = 8.sdp, bottomEnd = 8.sdp
                )
            ) {
                Text("Reset Password")
            }

        }
        // Back Arrow Button
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .padding(top = 32.sdp, start = 16.sdp)
                .align(Alignment.TopStart)
                .clickable {
                    onBackClick()
                }
        )
    }
}

@Composable
@Preview
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        modifier = Modifier,
        onBackClick = {},
        onResetPasswordClick = {}
    )
}