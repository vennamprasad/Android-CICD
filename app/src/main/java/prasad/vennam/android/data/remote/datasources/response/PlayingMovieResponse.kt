package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class PlayingMovieResponse(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("poster_path")
    val posterPath: String? = null,
)