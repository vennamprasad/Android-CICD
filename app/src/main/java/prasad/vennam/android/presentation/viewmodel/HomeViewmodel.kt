package prasad.vennam.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import prasad.vennam.android.data.remote.datasources.MovieRemoteRepository
import prasad.vennam.android.data.remote.datasources.response.NowPlayingMovieListResponse
import prasad.vennam.android.data.remote.datasources.response.UpcomingMovieListResponse
import prasad.vennam.android.domain.model.CommonMovie
import prasad.vennam.android.domain.model.TrendingMovie
import prasad.vennam.android.utils.ListState
import prasad.vennam.android.utils.Status
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val myListMovieRepository: MovieLocalRepository
) : ViewModel() {


    private val _trendingMovieListState =
        MutableStateFlow<ListState>(ListState.Loading)

    val trendingMovieListState: StateFlow<ListState> = _trendingMovieListState

    private val _upcomingMovieListState =
        MutableStateFlow<ViewState<List<CommonMovie>>>(ViewState.loading())
    val upcomingMovieListState: StateFlow<ViewState<List<CommonMovie>>> = _upcomingMovieListState

    private val _nowPlayingMovieListState =
        MutableStateFlow<ViewState<List<CommonMovie>>>(ViewState.loading())
    val nowPlayingMovieListState: StateFlow<ViewState<List<CommonMovie>>> =
        _nowPlayingMovieListState


    init {
        loadTrendingMovieList()
        loadUpcomingMovieList()
        loadNowPlayingMovieList()
    }

    private fun loadNowPlayingMovieList() {
        val nowPlayingMoviesFlow: Flow<ViewState<NowPlayingMovieListResponse>> =
            movieRemoteRepository.fetchNowPlayingMovies()

        viewModelScope.launch {
            combine(
                nowPlayingMoviesFlow,
                getSavedMovies()
            ) { apiResponse, savedMovies ->
                when (apiResponse.status) {
                    Status.SUCCESS -> {
                        val savedIds = savedMovies.map { it.id }.toSet()
                        ViewState.success(
                            apiResponse.data?.results?.map { movie ->
                                CommonMovie(
                                    id = movie.id ?: 0,
                                    poster = movie.posterPath.orEmpty(),
                                    isBookmarked = savedIds.contains(movie.id)
                                )
                            } ?: emptyList()
                        )
                    }

                    Status.ERROR -> ViewState.error(apiResponse.message.orEmpty())
                    Status.LOADING -> ViewState.loading()
                }
            }.catch {
                _nowPlayingMovieListState.value = ViewState.error(it.message.orEmpty())
            }.collect {
                _nowPlayingMovieListState.value = it
            }
        }
    }

    private fun loadUpcomingMovieList() {
        val upcomingMoviesFlow: Flow<ViewState<UpcomingMovieListResponse>> =
            movieRemoteRepository.fetchUpcomingMovies()

        viewModelScope.launch {
            combine(
                upcomingMoviesFlow,
                getSavedMovies()
            ) { apiResponse, savedMovies ->
                when (apiResponse.status) {
                    Status.SUCCESS -> {
                        val savedIds = savedMovies.map { it.id }.toSet()
                        ViewState.success(
                            apiResponse.data?.results?.map { movie ->
                                CommonMovie(
                                    id = movie.id ?: 0,
                                    poster = movie.posterPath.orEmpty(),
                                    isBookmarked = savedIds.contains(movie.id)
                                )
                            } ?: emptyList()
                        )
                    }

                    Status.ERROR -> ViewState.error(apiResponse.message.orEmpty())
                    Status.LOADING -> ViewState.loading()
                }
            }.catch {
                _upcomingMovieListState.value = ViewState.error(it.message.orEmpty())
            }.collect {
                _upcomingMovieListState.value = it
            }
        }
    }

    private fun loadTrendingMovieList() {
        _trendingMovieListState.value = ListState.Loading
        viewModelScope.launch {
            val pagerFlow: Flow<PagingData<TrendingMovie>> =
                combine(
                    movieRemoteRepository.getPagerMovies(),
                    getSavedMovies()
                ) { pagingData, savedMovies ->
                    val savedIds = savedMovies.map { it.id }.toSet()
                    pagingData.map { movie ->
                        TrendingMovie(
                            id = movie.id ?: 0,
                            title = movie.title.orEmpty(),
                            voteAverage = movie.voteAverage ?: 0.0,
                            originalLanguage = movie.originalLanguage.orEmpty(),
                            posterPath = movie.posterPath,
                            backdropPath = movie.backdropPath,
                            overview = movie.overview.orEmpty(),
                            isSaved = savedIds.contains(movie.id)
                        )
                    }
                }
                    .catch { _trendingMovieListState.value = ListState.Error(it) }
                    .cachedIn(viewModelScope)

            _trendingMovieListState.value = ListState.Success(pagerFlow)
        }
    }

    fun getSavedMovies(): Flow<List<CommonMovie>> {
        return myListMovieRepository.getAllSavedMovies().map { movieEntities ->
            movieEntities.map { movieEntity ->
                CommonMovie(
                    id = movieEntity.id,
                    poster = movieEntity.posterPath
                )
            }
        }
    }

    fun addToWatchList(mediaId: Int) {
        _nowPlayingMovieListState.value.data?.let { movies ->
            val movie = movies.find { it.id == mediaId }
            if (movie != null) {
                viewModelScope.launch {
                    myListMovieRepository.saveMovie(
                        MovieEntity(
                            id = movie.id,
                            posterPath = movie.poster
                        )
                    )
                }
            }
        }
    }

}