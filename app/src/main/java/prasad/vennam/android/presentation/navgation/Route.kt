package prasad.vennam.android.presentation.navgation

import java.net.URL

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Onboarding : Route("onboarding")
    data object Login : Route("login")
    data object SignUp : Route("signup")
    data object ForgotPassword : Route("forgot_password")
    data object ResetPassword : Route("reset_password")
    data object Home : Route("home")
    data object Profile : Route("profile")
    data object Settings : Route("settings")
    data object About : Route("about")
    data object ContactUs : Route("contact_us")
    data object Feedback : Route("feedback")
    data object Help : Route("help")
    data object Notifications : Route("notifications")
    data object Search : Route("search")
    data class ExternalWebViewWithUrl(val url: URL) : Route("webViewWithUrl")

}