package prasad.vennam.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCast(
    val id: Int,
    val name: String,
    val gender: Int,
    val profilePath: String? = null,
    val character: String,
) : Parcelable