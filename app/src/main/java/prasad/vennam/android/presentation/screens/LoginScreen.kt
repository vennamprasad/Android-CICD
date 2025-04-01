package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import prasad.vennam.android.ui.theme.sdp
import prasad.vennam.android.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.sdp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { androidx.compose.material3.Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .padding(8.sdp)
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { androidx.compose.material3.Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(
            modifier = Modifier
                .padding(8.sdp)
        )
        androidx.compose.material3.ElevatedButton(
            onClick = {
                viewModel.apply {
                    saveUserData(
                        email = email,
                        password = password
                    )
                }
                onLoginSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            androidx.compose.material3.Text("Login")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onForgotPasswordClick = {},
        onSignUpClick = {}
    )
}
