package prasad.vennam.android.data.remote.datasources.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PersonCredits(
    val cast: List<PersonMovieCredit>,
    val crew: List<PersonMovieCredit>
)

@Serializable
data class PersonMovieCredit(
    val id: Int,
    val title: String,
    @SerialName("original_title") val originalTitle: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    val popularity: Double,
    @SerialName("genre_ids") val genreIds: List<Int>,
    val adult: Boolean,
    val character: String?,
    val job: String?,
    @SerialName("credit_id") val creditId: String
)