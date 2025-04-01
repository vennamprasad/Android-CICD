package prasad.vennam.android.utils

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import prasad.vennam.android.domain.model.TrendingMovie

sealed class ListState {
    data object Loading : ListState()
    data class Success(val pagingData: Flow<PagingData<TrendingMovie>>) : ListState()
    data class Error(val error: Throwable) : ListState()
}