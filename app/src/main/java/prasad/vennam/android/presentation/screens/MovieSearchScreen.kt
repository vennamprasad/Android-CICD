package prasad.vennam.android.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import prasad.vennam.android.data.remote.datasources.response.MovieSearchFilters
import prasad.vennam.android.data.remote.datasources.response.SearchResponse
import prasad.vennam.android.presentation.components.CommonPosterCard
import prasad.vennam.android.presentation.viewmodel.MovieSearchViewModel

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun MovieSearchScreen(
    onBackClick: () -> Unit, viewModel: MovieSearchViewModel, onMovieClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var includeAdult by remember { mutableStateOf(false) }
    var region by remember { mutableStateOf<String?>(null) }
    var year by remember { mutableStateOf<String?>(null) }
    var appliedFilters by remember { mutableStateOf(MovieSearchFilters()) }

    val lazyPagingItems = viewModel.searchMoviesPaged(appliedFilters).collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }.debounce(500).filter { it.length >= 3 }.distinctUntilChanged()
            .collect { query ->
                appliedFilters = MovieSearchFilters(query = query)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search movies...") },
                    singleLine = true
                )
            }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    appliedFilters = MovieSearchFilters(
                        query = searchQuery,
                        includeAdult = includeAdult,
                        region = region?.takeIf { it.isNotBlank() },
                        year = year?.toIntOrNull()
                    )
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            })
        }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            if (searchQuery.length >= 3) {
                MovieSearchResults(
                    lazyPagingItems = lazyPagingItems, onMovieClick = onMovieClick
                )
            } else {
                Text(
                    text = "Enter at least 3 characters", modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MovieSearchResults(
    lazyPagingItems: LazyPagingItems<SearchResponse>,
    onMovieClick: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                CommonPosterCard(
                    id = item.id ?: 0,
                    poster = item.posterPath.orEmpty(),
                    onItemClick = onMovieClick,
                    modifier = Modifier.padding(8.dp),
                    onItemClickWatchList = {
                        onMovieClick
                    }
                )
            }
        }
    }
}