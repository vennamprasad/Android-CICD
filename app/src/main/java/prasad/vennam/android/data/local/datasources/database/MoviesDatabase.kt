package prasad.vennam.android.data.local.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import prasad.vennam.android.data.local.datasources.dao.MovieDao
import prasad.vennam.android.data.local.datasources.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favoriteMovies(): MovieDao
}