package prasad.vennam.android.utils

import prasad.vennam.android.BuildConfig

fun getBackgroundImageUrl(path: String?, size: String = DEFAULT): String {
    return path?.let {
        BuildConfig.IMAGE_BASE_URL + size + path
    } ?: ""
}

const val PROFILE_WIDTH = "w200"
const val BACKDROP_WIDTH = "w1280"
const val POSTER_WIDTH = "w500"
const val DEFAULT = "original"

