package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.MovieSearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchResponse
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val movieRepository: MovieRemoteRepository,
) : ViewModel() {

    fun searchMoviesPaged(filters: MovieSearchFilters): Flow<PagingData<SearchResponse>> {
        return movieRepository
            .searchMoviesPaged(filters)
            .cachedIn(viewModelScope)
    }
}