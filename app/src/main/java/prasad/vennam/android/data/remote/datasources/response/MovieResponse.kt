package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

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

    @SerializedName("adult")
    val adult: Boolean? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    @SerializedName("popularity")
    val popularity: Double? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("video")
    val video: Boolean? = null,

    @SerializedName("vote_count")
    val voteCount: Int? = null,

    @SerializedName("is_saved")
    var isSaved: Boolean = false
)