package prasad.vennam.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonMovie(
    val id: Int,
    val poster: String,
    val isBookmarked: Boolean = false
) : Parcelable