package prasad.vennam.android.data.remote.datasources.response

data class MovieSearchFilters(
    val query: String = "",
    val page: Int = 1,
    val includeAdult: Boolean = false,
    val region: String? = null,
    val year: Int? = null,
    val primaryReleaseYear: Int? = null,
    val sortBy: String = "popularity.desc", // popularity.desc, release_date.desc, etc.
    val withGenres: List<Int> = emptyList(),
    val withoutGenres: List<Int> = emptyList(),
    val voteAverageGte: Double? = null,
    val voteAverageLte: Double? = null,
    val voteCountGte: Int? = null,
    val releaseDateGte: String? = null, // Format: YYYY-MM-DD
    val releaseDateLte: String? = null,
    val withRuntimeGte: Int? = null,
    val withRuntimeLte: Int? = null
) {

    // Builder-style setters
    fun withQuery(query: String) = copy(query = query)
    fun page(page: Int) = copy(page = page)
    fun includeAdult(include: Boolean) = copy(includeAdult = include)
    fun region(region: String?) = copy(region = region)
    fun year(year: Int?) = copy(year = year)
    fun primaryReleaseYear(year: Int?) = copy(primaryReleaseYear = year)
    fun sortBy(sortBy: String) = copy(sortBy = sortBy)

    fun withGenre(genreId: Int) = copy(withGenres = withGenres + genreId)
    fun withGenres(genres: List<Int>) = copy(withGenres = withGenres + genres)
    fun withoutGenre(genreId: Int) = copy(withoutGenres = withoutGenres + genreId)
    fun withoutGenres(genres: List<Int>) = copy(withoutGenres = withoutGenres + genres)

    fun voteAverageGte(value: Double) = copy(voteAverageGte = value)
    fun voteAverageLte(value: Double) = copy(voteAverageLte = value)
    fun voteCountGte(value: Int) = copy(voteCountGte = value)
}

object SearchFilters {

    fun toQueryMap(filters: MovieSearchFilters): Map<String, String> {
        val queryMap = mutableMapOf<String, String>()

        queryMap["query"] = filters.query
        queryMap["page"] = filters.page.toString()
        queryMap["include_adult"] = filters.includeAdult.toString()

        filters.region?.let { queryMap["region"] = it }
        filters.year?.let { queryMap["year"] = it.toString() }
        filters.primaryReleaseYear?.let { queryMap["primary_release_year"] = it.toString() }
        queryMap["sort_by"] = filters.sortBy

        if (filters.withGenres.isNotEmpty()) {
            queryMap["with_genres"] = filters.withGenres.joinToString(",")
        }
        if (filters.withoutGenres.isNotEmpty()) {
            queryMap["without_genres"] = filters.withoutGenres.joinToString(",")
        }

        filters.voteAverageGte?.let { queryMap["vote_average.gte"] = it.toString() }
        filters.voteAverageLte?.let { queryMap["vote_average.lte"] = it.toString() }
        filters.voteCountGte?.let { queryMap["vote_count.gte"] = it.toString() }

        filters.releaseDateGte?.let { queryMap["release_date.gte"] = it }
        filters.releaseDateLte?.let { queryMap["release_date.lte"] = it }

        filters.withRuntimeGte?.let { queryMap["with_runtime.gte"] = it.toString() }
        filters.withRuntimeLte?.let { queryMap["with_runtime.lte"] = it.toString() }

        return queryMap
    }
}
