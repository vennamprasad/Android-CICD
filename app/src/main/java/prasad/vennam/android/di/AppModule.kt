package prasad.vennam.android.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import prasad.vennam.android.BuildConfig
import prasad.vennam.android.data.local.datasources.dao.MovieDao
import prasad.vennam.android.data.local.datasources.database.MoviesDatabase
import prasad.vennam.android.data.local.datasources.model.MovieEntity
import prasad.vennam.android.data.local.datasources.repository.MovieLocalRepository
import prasad.vennam.android.data.remote.datasources.MovieService
import prasad.vennam.android.utils.NetworkConnectionInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val CONNECT_TIMEOUT = 20L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 120L

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val headersInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                BuildConfig.Authorization
            )
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun okHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headersInterceptor)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MoviesDatabase::class.java, "movies"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    @Singleton
    fun provideDao(db: MoviesDatabase) = db.favoriteMovies()

    @Provides
    fun provideEntity() = MovieEntity()

    @Singleton
    @Provides
    fun provideMovieLocalRepository(movieDao: MovieDao): MovieLocalRepository =
        MovieLocalRepository(movieDao)

}