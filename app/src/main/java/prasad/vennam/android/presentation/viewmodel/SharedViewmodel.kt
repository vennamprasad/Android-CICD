package prasad.vennam.android.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import prasad.vennam.android.utils.NetworkType
import prasad.vennam.android.utils.NetworkUtils
import javax.inject.Inject

@HiltViewModel
class SharedViewmodel @Inject constructor(
    private val application: Application
) : ViewModel() {
    fun checkNetworkStatus() {
        NetworkUtils.isNetworkAvailable(application)
    }

    val networkType: StateFlow<NetworkType> = NetworkUtils.networkType
}

