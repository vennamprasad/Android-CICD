package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import prasad.vennam.android.ui.theme.primaryDark
import prasad.vennam.android.ui.theme.primaryLight

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    onExternalLink: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            primaryLight,
                            primaryDark
                        )
                    )
                ),
        ) {
        }
    }
}