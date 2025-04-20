package prasad.vennam.android.domain.mappers


import prasad.vennam.android.data.remote.datasources.response.MovieDetailResponse
import prasad.vennam.android.data.remote.datasources.response.SingleCastResponse
import prasad.vennam.android.data.remote.datasources.response.TrendingMovieResponse
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.domain.model.MovieCast
import prasad.vennam.android.domain.model.MovieFullDetails
import prasad.vennam.android.domain.model.TrendingMovie

fun mapToMovieFullDetails(
    movieDetail: MovieDetailResponse,
    castList: List<SingleCastResponse>,
    similarMovies: List<TrendingMovieResponse>
): MovieFullDetails {
    return MovieFullDetails(
        id = movieDetail.id,
        title = movieDetail.title,
        voteAverage = movieDetail.voteAverage,
        originalLanguage = movieDetail.originalLanguage,
        backdropPath = movieDetail.backdropPath.orEmpty(),
        overview = movieDetail.overview,
        genres = movieDetail.genres?.map {
            Genre(id = it.id, name = it.name)
        } ?: emptyList(),
        posterPath = movieDetail.posterPath.orEmpty(),
        castList = castList.sortedByDescending {
            it.profilePath
        }.map {
            MovieCast(
                id = it.id ?: 0,
                name = it.name.orEmpty(),
                gender = it.gender ?: 0,
                profilePath = it.profilePath.orEmpty(),
                character = it.character.orEmpty()
            )
        },
        similarMovies = similarMovies.map {
            TrendingMovie(
                id = it.id ?: 0,
                title = it.title.orEmpty(),
                voteAverage = it.voteAverage ?: 0.0,
                originalLanguage = it.originalLanguage.orEmpty(),
                posterPath = it.posterPath.orEmpty(),
                backdropPath = it.backdropPath.orEmpty(),
                overview = it.overview.orEmpty(),
                isSaved = false
            )
        },
        releaseDate = movieDetail.releaseDate,
        runtime = movieDetail.runtime,
        tagline = movieDetail.tagline,
        spokenLanguages = movieDetail.spokenLanguages.joinToString(", ") { it.englishName },
        budget = movieDetail.budget,
        revenue = movieDetail.revenue,
        productionCompanies = movieDetail.productionCompanies.joinToString(", ") { it.name },
        productionCountries = movieDetail.productionCountries.joinToString(", ") { it.name },
        externalLink = movieDetail.homepage,
        adult = movieDetail.adult,
        originCountry = movieDetail.originCountry.joinToString(", ") { it },
        imdbId = movieDetail.imdbId,
        originalTitle = movieDetail.originalTitle,
        popularity = movieDetail.popularity,
        status = movieDetail.status,
        video = movieDetail.video,
        voteCount = movieDetail.voteCount,
        productionCountriesName = movieDetail.productionCountries.joinToString(", ") { it.name },
    )
}
