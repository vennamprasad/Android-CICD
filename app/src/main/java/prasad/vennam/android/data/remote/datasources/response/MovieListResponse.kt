package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class MovieListResponse(

    @SerializedName("dates")
    val dates: DatesResponse? = null,

    @SerializedName("results")
    val results: List<MovieResponse>? = null,
)