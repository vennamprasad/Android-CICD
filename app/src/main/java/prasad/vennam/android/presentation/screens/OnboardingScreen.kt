package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import prasad.vennam.android.R
import prasad.vennam.android.presentation.navgation.Route
import prasad.vennam.android.ui.theme.sdp

@Composable
fun OnboardingScreen(
    modifier: Modifier,
    onClick: (String) -> Unit
) {

    val listOfOnboarding = listOf(
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
    )
    val listOfPageTypes = listOf(
        "Login", "SignUp", "Home"
    )
    val pagerState = rememberPagerState {
        listOfOnboarding.size
    }
    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize(),
        ) { pageIndex ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                OnboardingPage(
                    image = listOfOnboarding[pageIndex],
                    text = listOfPageTypes[pageIndex],
                    onClick = {
                        when (pagerState.currentPage) {
                            0 -> onClick(Route.Login.route)
                            1 -> onClick(Route.SignUp.route)
                            2 -> onClick(Route.Home.route)
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(PaddingValues(horizontal = 16.sdp))
                .align(Alignment.TopEnd)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Button(
                onClick = {
                    onClick(Route.Home.route)
                },
                modifier = Modifier,
            ) {
                Text("Skip")
            }
        }
    }
}

@Composable
fun OnboardingPage(image: Int, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.Center
                )
                .fillMaxSize()
        )
        androidx.compose.material3.Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 16.sdp, vertical = 48.sdp))
                .align(Alignment.BottomCenter)
        ) {
            Text(text)
        }
    }
}
