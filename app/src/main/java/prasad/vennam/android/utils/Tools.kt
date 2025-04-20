package prasad.vennam.android.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import prasad.vennam.android.BuildConfig
import java.text.DateFormat.getDateTimeInstance
import java.util.Date

fun openLink(mContext: Context, url: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = url.toUri()
    mContext.startActivity(openURL)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = getDateTimeInstance().format("dd/MM/yyyy HH:mm:ss")
    return format.format(date)
}

fun getBackgroundImageUrl(path: String?, size: String = DEFAULT): String {
    return path?.let {
        BuildConfig.IMAGE_BASE_URL + size + path
    } ?: ""
}

fun <T> Flow<T>.pairWithPrevious(): Flow<Pair<T, T>> = flow {
    var previous: T? = null
    collect { value ->
        previous?.let { emit(it to value) }
        previous = value
    }
}