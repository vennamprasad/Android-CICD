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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
            nowPlayingMoviesFlow.catch {
                _nowPlayingMovieListState.value = ViewState.error(it.message.orEmpty())
            }.collect { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        _nowPlayingMovieListState.value =
                            ViewState.success(response.data?.results?.map { movie ->
                                CommonMovie(
                                    id = movie.id ?: 0, poster = movie.posterPath.orEmpty()
                                )
                            } ?: emptyList())
                    }

                    Status.ERROR -> {
                        _nowPlayingMovieListState.value =
                            ViewState.error(response.message.orEmpty())
                    }

                    Status.LOADING -> {
                        _nowPlayingMovieListState.value = ViewState.loading()
                    }
                }
            }
        }
    }

    private fun loadUpcomingMovieList() {
        val upcomingMoviesFlow: Flow<ViewState<UpcomingMovieListResponse>> =
            movieRemoteRepository.fetchUpcomingMovies()

        viewModelScope.launch {
            upcomingMoviesFlow.catch {
                _upcomingMovieListState.value = ViewState.error(it.message.orEmpty())
            }.collect { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        _upcomingMovieListState.value =
                            ViewState.success(response.data?.results?.map { movie ->
                                CommonMovie(
                                    id = movie.id ?: 0,
                                    poster = movie.posterPath.orEmpty(),
                                )
                            } ?: emptyList())
                    }

                    Status.ERROR -> {
                        _upcomingMovieListState.value = ViewState.error(response.message.orEmpty())
                    }

                    Status.LOADING -> {
                        _upcomingMovieListState.value = ViewState.loading()
                    }
                }
            }
        }
    }

    private fun loadTrendingMovieList() {
        _trendingMovieListState.value = ListState.Loading
        viewModelScope.launch {
            val pagerFlow: Flow<PagingData<TrendingMovie>> = movieRemoteRepository.getPagerMovies()
                .catch {
                    _trendingMovieListState.value = ListState.Error(it)
                }
                .map { pagingData ->
                    pagingData.map { movie ->
                        TrendingMovie(
                            id = movie.id ?: 0,
                            title = movie.title.orEmpty(),
                            voteAverage = movie.voteAverage ?: 0.0,
                            originalLanguage = movie.originalLanguage.orEmpty(),
                            posterPath = movie.posterPath,
                            backdropPath = movie.backdropPath,
                            overview = movie.overview.orEmpty(),
                            isSaved = false
                        )
                    }
                }
                .cachedIn(viewModelScope)
            _trendingMovieListState.value = ListState.Success(pagerFlow)
        }
    }
}