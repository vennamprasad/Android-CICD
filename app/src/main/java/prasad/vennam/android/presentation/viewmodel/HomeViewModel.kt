package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.MovieListResponse
import prasad.vennam.android.data.remote.datasources.response.MovieResponse
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository
) : ViewModel() {

    private val savedMoviesFlow =
        movieLocalRepository.getAllSavedMovies().map { entities -> entities.map { it.id }.toSet() }
            .shareIn(
                scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), replay = 1
            )


    private val _trendingMovieListState =
        MutableStateFlow<ViewState<Flow<PagingData<TrendingMovie>>>>(ViewState.loading())
    private val _upcomingMovieListState =
        MutableStateFlow<ViewState<List<CommonMovie>>>(ViewState.loading())

    private val _nowPlayingMovieListState =
        MutableStateFlow<ViewState<List<CommonMovie>>>(ViewState.loading())

    data class HomeUiState(
        val trendingMovies: ViewState<Flow<PagingData<TrendingMovie>>> = ViewState.loading(),
        val upcomingMovies: ViewState<List<CommonMovie>> = ViewState.loading(),
        val nowPlayingMovies: ViewState<List<CommonMovie>> = ViewState.loading(),
    )

    val uiState: StateFlow<HomeUiState> = combine(
        _trendingMovieListState.asStateFlow(),
        _upcomingMovieListState.asStateFlow(),
        _nowPlayingMovieListState.asStateFlow()
    ) { trending, upcoming, nowPlaying ->
        HomeUiState(
            trendingMovies = trending,
            upcomingMovies = upcoming,
            nowPlayingMovies = nowPlaying
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    init {
        loadAllMovieData()
    }

    private fun loadAllMovieData() {
        loadTrendingMoviesList()
        loadUpcomingMovieList()
        loadNowPlayingMovieList()
    }

    private fun loadTrendingMoviesList() {
        val trendingMoviesPagingFlow: Flow<PagingData<TrendingMovie>> = combine(
            movieRemoteRepository.getPagerMovies(), savedMoviesFlow
        ) { pagingData, savedMovieIds ->
            pagingData.map { movie ->
                movie.toTrendingMovie(savedMovieIds.contains(movie.id))
            }
        }.cachedIn(viewModelScope)
        viewModelScope.launch {
            movieRemoteRepository.fetchTrendingMovieListData()
                .catch { exception ->
                    _trendingMovieListState.value = ViewState.error(exception.message.orEmpty())
                }
                .collect { response ->
                    _trendingMovieListState.value = when (response.status) {
                        Status.SUCCESS -> ViewState.success(trendingMoviesPagingFlow)
                        Status.ERROR -> ViewState.error(response.message.orEmpty())
                        Status.LOADING -> ViewState.loading()
                    }
                }
        }
    }

    private fun loadUpcomingMovieList() {
        viewModelScope.launch {
            combine(
                movieRemoteRepository.fetchUpcomingMovies(), savedMoviesFlow
            ) { apiResponse, savedMovieIds ->
                mapMovieResponse(apiResponse, savedMovieIds)
            }.catch { exception ->
                emit(ViewState.error(exception.message.orEmpty()))
            }.collect { viewState ->
                _upcomingMovieListState.value = viewState
            }
        }
    }

    private fun loadNowPlayingMovieList() {
        viewModelScope.launch {
            combine(
                movieRemoteRepository.fetchNowPlayingMovies(), savedMoviesFlow
            ) { apiResponse, savedMovieIds ->
                mapMovieResponse(apiResponse, savedMovieIds)
            }.catch { exception ->
                emit(ViewState.error(exception.message.orEmpty()))
            }.collect { viewState ->
                _nowPlayingMovieListState.value = viewState
            }
        }
    }

    private fun mapMovieResponse(
        apiResponse: ViewState<MovieListResponse>, savedMovieIds: Set<Int>
    ): ViewState<List<CommonMovie>> {
        return when (apiResponse.status) {
            Status.SUCCESS -> {
                val movies = apiResponse.data?.results?.map { movie ->
                    movie.toCommonMovie(savedMovieIds.contains(movie.id))
                } ?: emptyList()
                ViewState.success(movies)
            }

            Status.ERROR -> ViewState.error(apiResponse.message.orEmpty())
            Status.LOADING -> ViewState.loading()
        }
    }

    fun refreshAllData() {
        loadUpcomingMovieList()
        loadNowPlayingMovieList()
    }

    fun refreshUpcomingMovies() {
        loadUpcomingMovieList()
    }

    fun refreshNowPlayingMovies() {
        loadNowPlayingMovieList()
    }

    fun refreshTrendingMovies() {
        loadTrendingMoviesList()
    }
}

private fun MovieResponse.toTrendingMovie(isSaved: Boolean): TrendingMovie {
    return TrendingMovie(
        id = id ?: 0,
        title = title.orEmpty(),
        voteAverage = voteAverage ?: 0.0,
        originalLanguage = originalLanguage.orEmpty(),
        posterPath = posterPath.orEmpty(),
        backdropPath = backdropPath.orEmpty(),
        overview = overview.orEmpty(),
        isSaved = isSaved
    )
}

private fun MovieResponse.toCommonMovie(isBookmarked: Boolean = false): CommonMovie {
    return CommonMovie(
        id = id ?: 0, poster = posterPath.orEmpty(), isBookmarked = isBookmarked
    )
}

private fun MovieEntity.toCommonMovie(): CommonMovie {
    return CommonMovie(
        id = this.id, poster = this.posterPath, isBookmarked = true
    )
}