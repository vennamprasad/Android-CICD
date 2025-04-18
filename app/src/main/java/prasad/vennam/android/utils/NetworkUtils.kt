package prasad.vennam.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object NetworkUtils {
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
    private val _networkType = MutableStateFlow("Unknown")
    val networkType: StateFlow<String> get() = _networkType

    @RequiresApi(Build.VERSION_CODES.S)
    fun registerNetworkCallback(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder().build()
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                CoroutineScope(Dispatchers.Default).launch {
                    _networkType.value = getNetworkType(networkCapabilities)
                }
            }

            override fun onAvailable(network: Network) {
                CoroutineScope(Dispatchers.Default).launch {
                    _networkType.value = "Connection is ON"
                }

            }

            override fun onLost(network: Network) {
                CoroutineScope(Dispatchers.Default).launch {
                    _networkType.value = "No Connection"
                }
            }
        }
        cm.registerNetworkCallback(request, callback)


    }


    private fun getNetworkType(networkCapabilities: NetworkCapabilities): String {
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Unknown"
        }
    }
}