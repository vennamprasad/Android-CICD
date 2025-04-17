package prasad.vennam.android.domain.model

import prasad.vennam.android.data.remote.datasources.response.ProductionCompany
import prasad.vennam.android.data.remote.datasources.response.ProductionCountry
import prasad.vennam.android.data.remote.datasources.response.SpokenLanguage

data class MovieFullDetails(
    val id: Int,
    val title: String,
    val voteAverage: Double,
    val originalLanguage: String,
    val backdropPath: String,
    val overview: String,
    val genres: List<Genre>,
    val posterPath: String,
    val castList: List<MovieCast>,
    val nowPlayingMovies: List<NowPlayingMovie>,
    val upComingMovies: List<UpcomingMovie>,
    val similarMovies: List<TrendingMovie>,
    val releaseDate: String,
    val runtime: Int,
    val tagline: String?,
    val externalLink: String? = null,
    val budget: Int = 0,
    val revenue: Int = 0,
    val productionCompanies: String = "",
    val productionCountries: String = "",
    val spokenLanguages: String = "",
    val adult: Boolean = false,
    val homepage: String? = null,
    val originCountry: String = "",
    val imdbId: String? = null,
    val originalTitle: String? = null,
    val popularity: Double = 0.0,
    val status: String? = null,
    val video: Boolean = false,
    val voteCount: Int = 0,
    val belongsToCollection: String? = null,
    val productionCountriesName: String? = null,
)