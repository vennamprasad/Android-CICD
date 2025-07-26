package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import prasad.vennam.android.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box {
        Column(
            modifier = modifier
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
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Forgot Password Button
            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.End)
            ) {
                Text("Forgot Password?")
            }

            // Sign In Button
            ElevatedButton(
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
                    .requiredHeight(
                        42.sdp
                    ),
                shape = RoundedCornerShape(
                    topStart = 8.sdp, topEnd = 8.sdp, bottomStart = 8.sdp, bottomEnd = 8.sdp
                )
            ) {
                Text("Sign In")
            }

            // new user? Sign Up Button
            TextButton(
                onClick = onSignUpClick,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("New User? Sign Up")
            }

            // Sign In with Google Button
            ElevatedButton(
                onClick = {
                    // Handle Google Sign In
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
                Text("Sign In with Google")
            }
            Spacer(
                modifier = Modifier
                    .padding(8.sdp)
            )
            // Sign In with Facebook Button
            ElevatedButton(
                onClick = {
                    // Handle Facebook Sign In
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
                Text("Sign In with Facebook")
            }
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
        modifier = Modifier,
        onLoginSuccess = {},
        onForgotPasswordClick = {},
        onSignUpClick = {}
    )
}
