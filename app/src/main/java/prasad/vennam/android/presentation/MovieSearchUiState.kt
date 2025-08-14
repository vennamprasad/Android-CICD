package prasad.vennam.android.presentation

import prasad.vennam.android.data.remote.datasources.response.Genre
import prasad.vennam.android.data.remote.datasources.response.MovieSearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchResponse

data class MovieSearchUiState(
    val isLoading: Boolean = false,
    val movies: List<SearchResponse> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val currentPage: Int = 1,
    val totalPages: Int = 0,
    val hasMore: Boolean = false,
    val error: String? = null,
    val filters: MovieSearchFilters = MovieSearchFilters()
)