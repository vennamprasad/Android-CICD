package prasad.vennam.android.domain.model

data class MovieFullDetails(
    val id: Int,
    val title: String,
    val voteAverage: Double,
    val originalLanguage: String,
    val backdropPath: String,
    val overview: String,
    val genres: List<Genre>,
    val posterPath: String,
    val castList: List<MovieCast>
)