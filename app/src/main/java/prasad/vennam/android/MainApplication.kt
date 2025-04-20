package prasad.vennam.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import prasad.vennam.android.utils.NetworkUtils

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkUtils.registerNetworkCallback(this)
    }
}