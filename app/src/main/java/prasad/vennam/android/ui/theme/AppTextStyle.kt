package prasad.vennam.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle


@Composable
fun TextStyle.BodyRegular() {
    this.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.ssp,
    )
}

@Composable
fun TextStyle.BodySmall() {
    this.copy(
        fontFamily = bodyFontFamily,
        fontSize = 14.ssp,
    )
}

@Composable
fun TextStyle.BodyLarge() {
    this.copy(
        fontFamily = bodyFontFamily,
        fontSize = 18.ssp,
    )
}

@Composable
fun TextStyle.BodyMedium() {
    this.copy(
        fontFamily = bodyFontFamily,
        fontSize = 14.ssp,
    )
}

@Composable
fun TextStyle.BodyXSmall() {
    this.copy(
        fontFamily = bodyFontFamily,
        fontSize = 10.ssp,
    )
}
