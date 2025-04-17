package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.CastsResponse
import prasad.vennam.android.data.remote.datasources.response.MovieDetailResponse
import prasad.vennam.android.data.remote.datasources.response.NowPlayingMovieListResponse
import prasad.vennam.android.data.remote.datasources.response.TrendingMovieListResponse
import prasad.vennam.android.data.remote.datasources.response.UpcomingMovieListResponse
import prasad.vennam.android.domain.mappers.mapToMovieFullDetails
import prasad.vennam.android.domain.model.MovieFullDetails
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewmodel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieFullDetailState =
        MutableStateFlow<ViewState<MovieFullDetails>>(ViewState.loading())
    val movieFullDetailState: StateFlow<ViewState<MovieFullDetails>> = _movieFullDetailState

    init {
        val movieId: Int = savedStateHandle["movieId"] ?: 0
        if (movieId > 0) {
            getMovieDetailsBy(movieId)
        }
    }

    private fun getMovieDetailsBy(id: Int) {
        viewModelScope.launch {
            try {
                val movieDetailFlow: Flow<ViewState<MovieDetailResponse>> =
                    movieRemoteRepository.fetchTrendingMovieDetailData(id)
                val movieCastFlow: Flow<ViewState<CastsResponse>> =
                    movieRemoteRepository.fetchMovieCast(id)
                val upcomingMoviesFlow: Flow<ViewState<UpcomingMovieListResponse>> =
                    movieRemoteRepository.fetchUpcomingMovies()
                val nowPlayingMoviesFlow: Flow<ViewState<NowPlayingMovieListResponse>> =
                    movieRemoteRepository.fetchNowPlayingMovies()
                val similarMoviesFlow: Flow<ViewState<TrendingMovieListResponse>> =
                    movieRemoteRepository.fetchSimilarMovies(id)

                combine(
                    movieDetailFlow,
                    movieCastFlow,
                    upcomingMoviesFlow,
                    nowPlayingMoviesFlow,
                    similarMoviesFlow
                ) { movieDetailState, castViewState, upcomingMoviesState, nowPlayingMovies, similarMovies ->

                    if (movieDetailState.data != null) {
                        val fullDetail: MovieFullDetails = mapToMovieFullDetails(
                            movieDetail = movieDetailState.data,
                            castList = castViewState.data?.cast ?: emptyList(),
                            nowPlayingMovies = nowPlayingMovies.data?.results ?: emptyList(),
                            upcomingMovies = upcomingMoviesState.data?.results ?: emptyList(),
                            similarMovies = similarMovies.data?.results ?: emptyList()
                        )
                        ViewState.success(fullDetail)
                    } else {
                        ViewState.error("Failed to fetch movie details")
                    }
                }.collect { state ->
                    _movieFullDetailState.value = state
                }

            } catch (e: Exception) {
                _movieFullDetailState.value = ViewState.error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}
