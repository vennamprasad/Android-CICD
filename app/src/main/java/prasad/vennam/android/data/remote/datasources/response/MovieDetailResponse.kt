package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("genres")
    val genreResponses: List<GenreResponse>? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("tagline")
    val tagline: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,
)