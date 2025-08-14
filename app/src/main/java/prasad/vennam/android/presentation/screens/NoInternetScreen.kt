package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import prasad.vennam.android.presentation.viewmodel.SharedViewmodel
import prasad.vennam.android.utils.NetworkType

@Composable
fun NetworkAwareScreen(
    viewModel: SharedViewmodel = hiltViewModel(), mainContent: @Composable () -> Unit
) {
    val networkStatus by viewModel.networkType.collectAsState()

    when (networkStatus) {
        NetworkType.UNKNOWN -> {
            NoInternetScreen(
                onRetryClick = {
                    viewModel.checkNetworkStatus()
                }
            )
        }

        else -> {
            mainContent()
        }

    }
}

@Composable
fun NoInternetScreen(
    onRetryClick: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("no_internet.json"))
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Internet Connection",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red
            )
        }

        Button(
            onClick = onRetryClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth(0.5f)
        ) {
            Text(
                text = "Retry",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}



@Composable
@Preview
fun NoInternetScreenPreview() {
    NetworkAwareScreen(hiltViewModel(), {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Main Content", color = Color.Green)
        }
    })
}