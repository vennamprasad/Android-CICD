package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class TrendingMovieResponse(

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("overview")
    val overview: String? = null,
)