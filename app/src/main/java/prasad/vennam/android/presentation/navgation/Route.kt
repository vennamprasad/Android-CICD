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
    data object MovieDetails : Route("movie_details")
    data object TVShowDetails : Route("tv_show_details")

    data object MovieCrewDetails : Route("movie_crew_details")
    data object TVShowCrewDetails : Route("tv_show_crew_details")

    data object MovieReviews : Route("movie_reviews")
    data object TVShowReviews : Route("tv_show_reviews")

    data object Watchlist : Route("watchlist")
    data object GenreWiseMovie : Route("genre_wise_movie")
    data object SeeAll : Route("see_all")
}