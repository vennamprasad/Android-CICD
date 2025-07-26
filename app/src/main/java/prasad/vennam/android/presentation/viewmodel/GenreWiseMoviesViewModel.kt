package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.NowPlayingMovieListResponse
import prasad.vennam.android.data.remote.datasources.response.TrendingMovieResponse
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject


@HiltViewModel
class GenreWiseMoviesViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository,
) : ViewModel() {
    private val _genreWiseMovies =
        MutableStateFlow<ViewState<List<TrendingMovieResponse>>>(ViewState.loading())
    val genreWiseMovies: StateFlow<ViewState<List<TrendingMovieResponse>>> = _genreWiseMovies

    fun fetchGenreWiseMovies(genreId: String) {
        movieRemoteRepository.fetchGenreWiseMovies(genreId = genreId)
        viewModelScope.launch {
            _genreWiseMovies.catch {
                _genreWiseMovies.value = ViewState.error(it.message.orEmpty())
            }.collect { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        _genreWiseMovies.value =
                            ViewState.success(response.data?.map { movie ->
                                TrendingMovieResponse(
                                    id = movie.id,
                                    title = movie.title,
                                    overview = movie.overview,
                                    posterPath = movie.posterPath,
                                    backdropPath = movie.backdropPath,
                                    voteAverage = movie.voteAverage,
                                )
                            } ?: emptyList())
                    }

                    Status.ERROR -> {
                        _genreWiseMovies.value = ViewState.error(response.message.orEmpty())
                    }

                    Status.LOADING -> {
                        _genreWiseMovies.value = ViewState.loading()
                    }
                }
            }
        }
    }
}
