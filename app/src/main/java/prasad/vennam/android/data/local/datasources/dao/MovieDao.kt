package prasad.vennam.android.data.local.datasources.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import prasad.vennam.android.data.local.datasources.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieEntity)

    @Delete
    fun delete(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): MovieEntity?

    @Query("SELECT*FROM movies")
    fun getAllSavedMovies(): List<MovieEntity>
}