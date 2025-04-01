package prasad.vennam.android.data.remote.datasources.response

import com.google.gson.annotations.SerializedName

data class TrendingMovieListResponse(

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<TrendingMovieResponse>? = null,
)