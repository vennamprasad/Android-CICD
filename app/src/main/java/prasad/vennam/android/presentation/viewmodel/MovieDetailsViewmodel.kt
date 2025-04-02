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
import prasad.vennam.android.domain.model.NowPlayingMovie
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.domain.model.UpcomingMovie
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
                val upcomingMoviesFlow = movieRemoteRepository.fetchUpcomingMovies()
                val nowPlayingMoviesFlow = movieRemoteRepository.fetchNowPlayingMovies()
                val similarMoviesFlow = movieRemoteRepository.fetchSimilarMovies(id)

                combine(
                    movieDetailFlow,
                    movieCastFlow,
                    upcomingMoviesFlow,
                    nowPlayingMoviesFlow,
                    similarMoviesFlow
                ) { detailViewState, castViewState, upcomingMoviesState, nowPlayingMovies, similarMovies ->
                    val movieDetail = detailViewState.data
                    val castList = castViewState.data?.cast ?: emptyList()

                    if (movieDetail != null) {
                        val fullDetail = MovieFullDetails(
                            id = movieDetail.id,
                            title = movieDetail.title,
                            voteAverage = movieDetail.voteAverage,
                            originalLanguage = movieDetail.originalLanguage,
                            backdropPath = movieDetail.backdropPath.orEmpty(),
                            overview = movieDetail.overview,
                            genres = movieDetail.genres?.map {
                                Genre(id = it.id, name = it.name)
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
                            },
                            nowPlayingMovies = nowPlayingMovies.data?.results?.map {
                                NowPlayingMovie(
                                    id = it.id ?: 0, poster = it.posterPath ?: ""
                                )
                            } ?: emptyList(),
                            upComingMovies = upcomingMoviesState.data?.results?.map {
                                UpcomingMovie(
                                    id = it.id ?: 0, poster = it.posterPath ?: ""
                                )
                            } ?: emptyList(),
                            similarMovies = similarMovies.data?.results?.map {
                                TrendingMovie(
                                    id = it.id ?: 0,
                                    title = it.title.orEmpty(),
                                    voteAverage = it.voteAverage ?: 0.0,
                                    originalLanguage = it.originalLanguage.orEmpty(),
                                    posterPath = it.posterPath.orEmpty(),
                                    backdropPath = it.backdropPath.orEmpty(),
                                    overview = it.overview.orEmpty(),
                                    isSaved = false
                                )
                            } ?: emptyList(),
                            releaseDate = movieDetail.releaseDate,
                            runtime = movieDetail.runtime,
                            tagline = movieDetail.tagline,
                            spokenLanguages = movieDetail.spokenLanguages.joinToString(", ") { it.englishName },
                            budget = movieDetail.budget,
                            revenue = movieDetail.revenue,
                            productionCompanies = movieDetail.productionCompanies.joinToString(", ") { it.name },
                            productionCountries = movieDetail.productionCountries.joinToString(", ") { it.name },
                            externalLink = movieDetail.homepage,
                            adult = movieDetail.adult,
                            originCountry = movieDetail.originCountry.joinToString(", ") { it },
                            imdbId = movieDetail.imdbId,
                            originalTitle = movieDetail.originalTitle,
                            popularity = movieDetail.popularity,
                            status = movieDetail.status,
                            video = movieDetail.video,
                            voteCount = movieDetail.voteCount,
                            productionCountriesName = movieDetail.productionCountries.joinToString(", ") { it.name },
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
