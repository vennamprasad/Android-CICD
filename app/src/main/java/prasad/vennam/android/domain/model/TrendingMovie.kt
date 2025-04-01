package prasad.vennam.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrendingMovie(
    val id: Int,
    val title: String,
    val voteAverage: Double,
    val originalLanguage: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String,
    var isSaved: Boolean,
) : Parcelable