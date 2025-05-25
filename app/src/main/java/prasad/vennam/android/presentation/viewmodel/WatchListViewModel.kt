package prasad.vennam.android.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(private val myListMovieRepository: MovieLocalRepository) :
    ViewModel() {
    private var _exist = mutableIntStateOf(0)
    val exist: State<Int> = _exist

    private val _myMovieData = mutableStateOf<Flow<List<MovieEntity>>>(emptyFlow())
    val myMovieData: State<Flow<List<MovieEntity>>> = _myMovieData

    init {
        allMovieData()
    }

    private fun allMovieData() {
        _myMovieData.value = myListMovieRepository.getAllSavedMovies()
    }

    private fun exist(mediaId: Int) {
        viewModelScope.launch {
            _exist.intValue = myListMovieRepository.exist(mediaId = mediaId)
        }
    }

    fun addToWatchList(movie: MovieEntity) {
        viewModelScope.launch {
            myListMovieRepository.saveMovie(movie)
        }.invokeOnCompletion {
            exist(movie.id)
        }
    }

    fun removeFromWatchList(mediaId: Int) {
        viewModelScope.launch {
            myListMovieRepository.deleteMovie(id = mediaId)
        }.invokeOnCompletion {
            exist(mediaId = mediaId)
        }
    }

}