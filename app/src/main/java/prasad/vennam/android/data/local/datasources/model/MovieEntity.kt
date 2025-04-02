package prasad.vennam.android.data.local.datasources.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double = 0.0,

    @ColumnInfo(name = "originalLanguage")
    val originalLanguage: String = "",

    @ColumnInfo(name = "overview")
    val overview: String = "",

    @ColumnInfo(name = "backdropPath")
    val backdropPath: String = "",

    @ColumnInfo(name = "posterPath")
    val posterPath: String = "",
)