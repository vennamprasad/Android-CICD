package prasad.vennam.android.data.remote.datasources.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import prasad.vennam.android.data.remote.datasources.MovieService
import prasad.vennam.android.data.remote.datasources.response.TrendingMovieResponse

class MoviePagingSource(private val apiService: MovieService) :
    PagingSource<Int, TrendingMovieResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrendingMovieResponse> {

        return try {
            val position = params.key ?: TMDB_STARTING_PAGE_INDEX
            val response = apiService.fetchAllTrendingMovies(position)
            LoadResult.Page(
                data = response.results!!, prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, TrendingMovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

private const val TMDB_STARTING_PAGE_INDEX = 1