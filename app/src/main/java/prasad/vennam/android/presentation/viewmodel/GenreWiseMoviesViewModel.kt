package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.MovieResponse
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject


@HiltViewModel
class GenreWiseMoviesViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository,
) : ViewModel() {
    private val _genreWiseMovies =
        MutableStateFlow<ViewState<List<MovieResponse>>>(ViewState.loading())
    val genreWiseMovies: StateFlow<ViewState<List<MovieResponse>>> = _genreWiseMovies

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
                                MovieResponse(
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
