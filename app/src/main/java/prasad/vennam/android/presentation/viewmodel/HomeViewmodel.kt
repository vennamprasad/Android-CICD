package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.utils.ListState
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
) : ViewModel() {


    private val _trendingMovieListState =
        MutableStateFlow<ListState>(ListState.Loading)

    val trendingMovieListState: StateFlow<ListState> = _trendingMovieListState

    init {
        loadTrendingMovieList()
    }

    private fun loadTrendingMovieList() {
        _trendingMovieListState.value = ListState.Loading

        viewModelScope.launch {
            val pagerFlow = movieRemoteRepository.getPagerMovies()
                .catch {
                    _trendingMovieListState.value = ListState.Error(it)

                }
                .map { pagingData ->

                    pagingData.map { trendingMovieResponse ->
                        TrendingMovie(
                            id = trendingMovieResponse.id ?: 0,
                            title = trendingMovieResponse.title.orEmpty(),
                            voteAverage = trendingMovieResponse.voteAverage ?: 0.0,
                            originalLanguage = trendingMovieResponse.originalLanguage.orEmpty(),
                            posterPath = trendingMovieResponse.posterPath,
                            backdropPath = trendingMovieResponse.backdropPath,
                            overview = trendingMovieResponse.overview.orEmpty(),
                            isSaved = false
                        )
                    }


                }
                .cachedIn(viewModelScope)


            _trendingMovieListState.value = ListState.Success(pagerFlow)

        }
    }

}