package prasad.vennam.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import prasad.vennam.android.presentation.navgation.AppNavGraph
import prasad.vennam.android.ui.theme.AppTheme
import prasad.vennam.android.ui.theme.ColorSchemeType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme(ColorSchemeType.DYNAMIC) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    content = { padding ->
                        AppNavGraph(
                            navController,
                            modifier = Modifier
                                .padding(padding)
                        )
                    }
                )
            }
        }
    }
}

