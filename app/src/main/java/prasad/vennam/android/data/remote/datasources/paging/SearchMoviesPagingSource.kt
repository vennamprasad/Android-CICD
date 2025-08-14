package prasad.vennam.android.data.remote.datasources.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import prasad.vennam.android.data.remote.datasources.MovieService
import prasad.vennam.android.data.remote.datasources.response.MovieSearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchResponse

class SearchMoviesPagingSource(
    private val apiService: MovieService,
    private val filters: MovieSearchFilters
) : PagingSource<Int, SearchResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResponse> {
        val page = params.key ?: 1
        return try {
            val queryParams = SearchFilters.toQueryMap(filters)
            val response = apiService.searchMovies(
                queryParams
            )
            LoadResult.Page(
                data = response.results.map { it.toMovieSearchResponse() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResponse>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }
}

private fun SearchResponse.toMovieSearchResponse(): SearchResponse {
    return this
}
