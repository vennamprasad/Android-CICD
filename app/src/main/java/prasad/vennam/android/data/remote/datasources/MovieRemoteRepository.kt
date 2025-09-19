package prasad.vennam.android.data.remote.datasources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import prasad.vennam.android.data.remote.datasources.paging.MoviePagingSource
import prasad.vennam.android.data.remote.datasources.paging.SearchMoviesPagingSource
import prasad.vennam.android.data.remote.datasources.response.CastsResponse
import prasad.vennam.android.data.remote.datasources.response.MovieDetailResponse
import prasad.vennam.android.data.remote.datasources.response.MovieListResponse
import prasad.vennam.android.data.remote.datasources.response.MovieResponse
import prasad.vennam.android.data.remote.datasources.response.MovieSearchFilters
import prasad.vennam.android.data.remote.datasources.response.MovieSearchResponse
import prasad.vennam.android.data.remote.datasources.response.PersonCredits
import prasad.vennam.android.data.remote.datasources.response.SearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchResponse
import prasad.vennam.android.utils.ViewState
import javax.inject.Inject

class MovieRemoteRepository @Inject constructor(
    private val movieService: MovieService,
) {

    fun fetchTrendingMovieListData(): Flow<ViewState<MovieListResponse>> {
        return flow {

            val trendingMovieListResponse = movieService.fetchAllTrendingMovies(1)

            emit(ViewState.success(trendingMovieListResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun getPagerMovies(): Flow<PagingData<MovieResponse>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { MoviePagingSource(movieService) }).flow.flowOn(Dispatchers.IO)
    }


    fun fetchTrendingMovieDetailData(id: Int): Flow<ViewState<MovieDetailResponse>> {
        return flow {
            val movieDetailResponse = movieService.fetchMovieDetailById(movieId = id)
            emit(ViewState.success(movieDetailResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun fetchMovieCast(id: Int): Flow<ViewState<CastsResponse>> {
        return flow {
            val castsResponse = movieService.fetchMovieCast(movieId = id)

            emit(ViewState.success(castsResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun fetchUpcomingMovies(): Flow<ViewState<MovieListResponse>> {
        return flow {
            val upcomingMoviesResponse = movieService.fetchUpcomingMovies()

            emit(ViewState.success(upcomingMoviesResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun fetchNowPlayingMovies(): Flow<ViewState<MovieListResponse>> {
        return flow {
            val nowPlayingMovieListResponse = movieService.fetchNowPlayingMovies()

            emit(ViewState.success(nowPlayingMovieListResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun fetchSimilarMovies(id: Int): Flow<ViewState<MovieListResponse>> {
        return flow {
            val similarMovieResponse = movieService.fetchSimilarMovies(movieId = id)

            emit(ViewState.success(similarMovieResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun fetchGenreWiseMovies(genreId: String): Flow<ViewState<MovieSearchResponse>> {
        return flow {
            val filters = MovieSearchFilters()
                .page(2)
                .withGenre(genreId.toInt())
            val queryParams = SearchFilters.toQueryMap(filters)
            val movieSearchResponse = movieService.discoverMovies(filters = queryParams)
            emit(ViewState.success(movieSearchResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun searchMovies(filters: MovieSearchFilters = MovieSearchFilters()): Flow<ViewState<MovieSearchResponse>> {
        return flow {
            val queryParams = SearchFilters.toQueryMap(filters)
            val movieSearchResponse = movieService.searchMovies(filters = queryParams)
            emit(ViewState.success(movieSearchResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun discoverMovies(filters: MovieSearchFilters): Flow<ViewState<MovieSearchResponse>> {
        return flow {
            emit(ViewState.loading())
            try {
                val queryParams = SearchFilters.toQueryMap(filters)
                val movieSearchResponse = movieService.discoverMovies(filters = queryParams)
                emit(ViewState.success(movieSearchResponse))
            } catch (e: Exception) {
                emit(ViewState.error(e.message ?: "An error occurred"))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchMoviesPaged(filters: MovieSearchFilters): Flow<PagingData<SearchResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                SearchMoviesPagingSource(movieService, filters)
            }
        ).flow
    }

    fun getPopularPeople(): Flow<ViewState<CastsResponse>> {
        return flow {
            val popularPeopleResponse = movieService.fetchPopularPeople(1)
            emit(ViewState.success(popularPeopleResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun getPersonMovieCredits(personId: Int): Flow<ViewState<PersonCredits>> {
        return flow {
            val personCredits = movieService.getPersonMovieCredits(personId)
            emit(ViewState.success(personCredits))
        }.flowOn(Dispatchers.IO)
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}