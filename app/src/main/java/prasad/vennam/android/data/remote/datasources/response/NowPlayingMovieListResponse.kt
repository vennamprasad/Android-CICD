package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class NowPlayingMovieListResponse(

    @SerializedName("dates")
    val dates: DatesResponse? = null,

    @SerializedName("results")
    val results: List<PlayingMovieResponse>? = null,
)