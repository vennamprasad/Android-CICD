package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.MovieResponse
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject


@HiltViewModel
class GenreWiseMoviesViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
) : ViewModel() {
    private val _genreWiseMovies =
        MutableStateFlow<ViewState<List<MovieResponse>>>(ViewState.loading())
    val genreWiseMovies: StateFlow<ViewState<List<MovieResponse>>> = _genreWiseMovies

    fun fetchGenreWiseMovies(genreId: String) {
        viewModelScope.launch {
            movieRemoteRepository.fetchGenreWiseMovies(genreId)
                .catch { throwable ->
                    _genreWiseMovies.value = ViewState.error(throwable.message.orEmpty())
                }
                .collect { response ->
                    when (response.status) {
                        Status.SUCCESS -> {
                            _genreWiseMovies.value =
                                ViewState.success(response.data?.results.orEmpty())
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
