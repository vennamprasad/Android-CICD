package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.domain.model.Genre
import prasad.vennam.android.domain.model.MovieCast
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
                val movieDetailFlow = movieRemoteRepository.fetchTrendingMovieDetailData(id)
                val movieCastFlow = movieRemoteRepository.fetchMovieCast(id)

                combine(movieDetailFlow, movieCastFlow) { detailViewState, castViewState ->
                    val movieDetail = detailViewState.data
                    val castList = castViewState.data?.cast ?: emptyList()

                    if (movieDetail != null) {
                        val fullDetail = MovieFullDetails(
                            id = movieDetail.id ?: 0,
                            title = movieDetail.title.orEmpty(),
                            voteAverage = movieDetail.voteAverage ?: 0.0,
                            originalLanguage = movieDetail.originalLanguage.orEmpty(),
                            backdropPath = movieDetail.backdropPath.orEmpty(),
                            overview = movieDetail.overview.orEmpty(),
                            genres = movieDetail.genreResponses?.map {
                                Genre(id = it.id ?: 0, name = it.name.orEmpty())
                            } ?: emptyList(),
                            posterPath = movieDetail.posterPath.orEmpty(),
                            castList = castList.sortedByDescending {
                                it.profilePath
                            }.map {
                                MovieCast(
                                    id = it.id ?: 0,
                                    name = it.name.orEmpty(),
                                    gender = it.gender ?: 0,
                                    profilePath = it.profilePath.orEmpty(),
                                    character = it.character.orEmpty()
                                )
                            }
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
