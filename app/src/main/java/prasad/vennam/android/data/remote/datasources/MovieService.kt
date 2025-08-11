package prasad.vennam.android.data.remote.datasources

import prasad.vennam.android.BuildConfig
import prasad.vennam.android.data.remote.datasources.response.CastsResponse
import prasad.vennam.android.data.remote.datasources.response.MovieDetailResponse
import prasad.vennam.android.data.remote.datasources.response.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {


    @GET("trending/movie/day?api_key=${BuildConfig.API_KEY}")
    suspend fun fetchAllTrendingMovies(@Query("page") page: Int): MovieListResponse

    @GET("movie/{movie_id}?api_key=${BuildConfig.API_KEY}")
    suspend fun fetchMovieDetailById(@Path("movie_id") movieId: Int): MovieDetailResponse

    @GET("movie/{movie_id}/credits?api_key=${BuildConfig.API_KEY}")
    suspend fun fetchMovieCast(@Path("movie_id") movieId: Int): CastsResponse

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovies(): MovieListResponse

    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovie(@Query("query") queryText: String): MovieListResponse

    @GET("movie/{movie_id}/similar?api_key=${BuildConfig.API_KEY}")
    suspend fun fetchSimilarMovies(@Path("movie_id") movieId: Int): MovieListResponse

    // popular people
    @GET("person/popular?api_key=${BuildConfig.API_KEY}")
    suspend fun fetchPopularPeople(@Query("page") page: Int): CastsResponse

    // genre wise movies
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: String,
        @Query("page") page: Int = 1,
    ): MovieListResponse


}