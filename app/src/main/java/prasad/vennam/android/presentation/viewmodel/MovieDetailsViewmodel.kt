package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
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
    private val movieLocalRepository: MovieLocalRepository,
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

    private fun getMovie(id: Int) {
        viewModelScope.launch {
            val movieEntity = movieLocalRepository.getMovie(id)
            if (movieEntity != null) {
                _movieFullDetailState.update { state ->
                    state.copy(
                        data = state.data?.copy(isSaved = true)
                    )
                }
            } else {
                _movieFullDetailState.update { state ->
                    state.copy(
                        data = state.data?.copy(isSaved = false)
                    )
                }
            }
        }
    }

    private fun getMovieDetailsBy(id: Int) {
        viewModelScope.launch {
            try {
                val movieDetailFlow: Flow<ViewState<MovieDetailResponse>> =
                    movieRemoteRepository.fetchTrendingMovieDetailData(id)
                val movieCastFlow: Flow<ViewState<CastsResponse>> =
                    movieRemoteRepository.fetchMovieCast(id)
                val similarMoviesFlow: Flow<ViewState<TrendingMovieListResponse>> =
                    movieRemoteRepository.fetchSimilarMovies(id)

                combine(
                    movieDetailFlow,
                    movieCastFlow,
                    similarMoviesFlow,
                ) { movieDetailState, castViewState, similarMovies ->
                    if (movieDetailState.data != null) {
                        val fullDetail: MovieFullDetails = mapToMovieFullDetails(
                            movieDetail = movieDetailState.data,
                            castList = castViewState.data?.cast ?: emptyList(),
                            similarMovies = similarMovies.data?.results ?: emptyList()
                        )
                        ViewState.success(fullDetail)
                    } else {
                        ViewState.error("Failed to fetch movie details")
                    }
                }.collect { state ->
                    _movieFullDetailState.value = state
                    getMovie(id)
                }

            } catch (e: Exception) {
                _movieFullDetailState.value = ViewState.error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    fun toggleMovieSavedStatus(movieId: Int) {
        viewModelScope.launch {
            if (_movieFullDetailState.value.data?.isSaved == true) {
                movieLocalRepository.deleteMovie(movieId)
            } else {
                val movieEntity = _movieFullDetailState.value.data?.toMovieEntity()
                if (movieEntity != null) {
                    movieLocalRepository.saveMovie(movieEntity)
                }
            }
            getMovie(movieId)
        }
    }
}

private fun MovieFullDetails.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        voteAverage = voteAverage,
        originalLanguage = originalLanguage,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
    )
}
