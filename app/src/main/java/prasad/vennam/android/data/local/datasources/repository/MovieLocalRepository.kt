package prasad.vennam.android.data.local.datasources.repository

import prasad.vennam.android.data.local.datasources.dao.MovieDao
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import javax.inject.Inject

class MovieLocalRepository @Inject constructor(private val dao: MovieDao) {
    fun saveMovie(movie: MovieEntity) = dao.insert(movie)
    fun deleteMovie(movie: MovieEntity) = dao.delete(movie)
    fun getMovie(id: Int): MovieEntity? = dao.getMovieById(id)
    fun getAllSavedMovies() = dao.getAllSavedMovies()
}