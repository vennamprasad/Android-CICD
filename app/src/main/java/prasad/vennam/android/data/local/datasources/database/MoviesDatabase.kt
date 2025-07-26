package prasad.vennam.android.data.local.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import prasad.vennam.android.data.local.datasources.converters.Converters
import prasad.vennam.android.data.local.datasources.dao.MovieDao
import prasad.vennam.android.data.local.datasources.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun favoriteMovies(): MovieDao
}