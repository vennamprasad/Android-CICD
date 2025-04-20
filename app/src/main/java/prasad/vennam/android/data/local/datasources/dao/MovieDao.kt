package prasad.vennam.android.data.local.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import prasad.vennam.android.data.local.datasources.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): MovieEntity?

    @Query("SELECT*FROM movies")
    fun getAllSavedMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies WHERE id = :movieId")
    fun deleteMovieById(movieId: Int)

    @Query("SELECT EXISTS (SELECT 1 FROM movies WHERE id = :mediaId)")
    suspend fun exists(mediaId: Int): Int
}