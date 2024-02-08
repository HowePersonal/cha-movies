package Util;

public final class SQLQueries {
    public static final String TOP_MOVIES_QUERY =  "SELECT top_movies.id, title, director, year, rating, \n" +
            "substring_index(group_concat(DISTINCT CONCAT(s.id, ':', s.name)), ',', 3) AS stars, \n" +
            "substring_index(group_concat(DISTINCT CONCAT(g.id, ':',g.name)), ',', 3) AS genres FROM \n" +
            "(SELECT * FROM movies m JOIN ratings r ON m.id = r.movieId order by rating desc limit 20) AS top_movies\n" +
            "JOIN genres_in_movies gm ON top_movies.id = gm.movieId\n" +
            "JOIN genres g ON gm.genreId = g.id\n" +
            "JOIN stars_in_movies sm ON top_movies.id = sm.movieId\n" +
            "JOIN stars s ON s.id = sm.starId\n" +
            "GROUP BY top_movies.id, top_movies.rating\n" +
            "order by top_movies.rating DESC;";

    public static final String SINGLE_MOVIE_QUERY = "SELECT m.id, m.title, m.year, m.director, r.rating, group_concat(DISTINCT CONCAT(g.id, ':',g.name)) genres, " +
            "group_concat(DISTINCT CONCAT(s.id, ':', s.name)) stars FROM\n" +
            "(SELECT * FROM movies m WHERE id = ?) m\n" +
            "LEFT JOIN ratings r ON m.id = r.movieId\n" +
            "JOIN genres_in_movies gm ON m.id = gm.movieId\n" +
            "JOIN genres g ON gm.genreId = g.id\n" +
            "JOIN stars_in_movies sm ON m.id = sm.movieId\n" +
            "JOIN stars s ON sm.starId = s.id\n" +
            "GROUP BY m.id, m.title, m.year, m.director, r.rating;";

    public static final String SINGLE_STAR_QUERY = "SELECT s.id, s.name, s.birthYear, group_concat(DISTINCT CONCAT(m.id, \";\", m.title)) movies FROM\n" +
            "(SELECT * from stars as s WHERE s.id = ?) s\n" +
            "JOIN stars_in_movies sm ON s.id = sm.starId\n" +
            "JOIN movies m ON sm.movieId = m.id\n" +
            "GROUP BY s.id;";

    public static final String ALL_STARS_QUERY = "SELECT * from stars;";

    public static final String SINGLE_GENRE_QUERY = "\n" +
            "WITH GenreMoviesFilter AS ( \n" +
            "                SELECT movieId \n" +
            "                FROM genres_in_movies \n" +
            "                WHERE genreId = ?\n" +
            "                GROUP BY movieId \n" +
            "            ),  \n" +
            "             \n" +
            "             \n" +
            "StarsRanked AS ( \n" +
            "SELECT sm.starId, s.name, count(sm.movieId) count \n" +
            "FROM GenreMoviesFilter gm \n" +
            "JOIN stars_in_movies sm ON sm.movieId = gm.movieId  \n" +
            "JOIN stars s ON s.id = sm.starId \n" +
            "GROUP BY sm.starId, s.name \n" +
            "), \n" +
            " \n" +
            "MovieGenres AS ( \n" +
            "SELECT gmf.movieId, group_concat(DISTINCT CONCAT(gim.genreId, ':', g.name) order by g.name) genres \n" +
            "FROM GenreMoviesFilter gmf, genres_in_movies gim, genres g \n" +
            "WHERE gmf.movieId = gim.movieId AND g.id = gim.genreId \n" +
            "GROUP BY gmf.movieId \n" +
            "), \n" +
            " \n" +
            "MoviesFiltered AS ( \n" +
            "SELECT mg.movieId, title, director, year, rating, mg.genres genres \n" +
            "FROM movies m \n" +
            "left JOIN ratings r ON m.id = r.movieId \n" +
            "\tJOIN MovieGenres mg ON mg.movieId = m.id \n" +
            ") \n" +
            " \n" +
            "SELECT m.movieId id, m.title, m.director, m.year, m.rating,  \n" +
            "\t   (SELECT substring_index(GROUP_CONCAT(DISTINCT CONCAT(sr.starId, ':', sr.name) ORDER BY sr.count DESC SEPARATOR ',' ), ',', 3)\n" +
            "\t\tFROM StarsRanked sr \n" +
            "\t\tJOIN stars_in_movies sm ON sr.starId = sm.starId AND sm.movieId = m.movieId \n" +
            "\t\tGROUP BY sm.movieId \n" +
            "\t\tLIMIT 3) AS stars,  \n" +
            "\t   m.genres \n" +
            "FROM MoviesFiltered m;";

    public static final String SINGLE_TITLE_QUERY = "SELECT m.id, m.title, m.year, m.director, r.rating, \n" +
            "group_concat(DISTINCT CONCAT(g.id, ':',g.name)) genres, \n" +
            "group_concat(DISTINCT CONCAT(s.id, ':', s.name)) stars FROM\n" +
            "(SELECT * FROM movies m WHERE m.title LIKE ?) m\n" +
            "JOIN ratings r ON m.id = r.movieId\n" +
            "JOIN genres_in_movies gm ON m.id = gm.movieId\n" +
            "JOIN genres g ON gm.genreId = g.id\n" +
            "JOIN stars_in_movies sm ON m.id = sm.movieId\n" +
            "JOIN stars s ON sm.starId = s.id\n" +
            "GROUP BY m.id, m.title, m.year, m.director, r.rating;";

    public String SEARCH_QUERY(String limit) {
        return
        "WITH MoviesFiltered AS (\n" +
                "    SELECT DISTINCT * FROM movies m \n" +
                "    WHERE MATCH(m.title) AGAINST (? IN BOOLEAN MODE) \n" +
                " LIMIT " + limit +
                "),\n" +
                "\n" +
                "StarsRanked AS ( \n" +
                "SELECT sm.starId, s.name, count(sm.movieId) count \n" +
                "FROM stars_in_movies sm\n" +
                "JOIN MoviesFiltered m on sm.movieId = m.id\n" +
                "JOIN stars s ON s.id = sm.starId \n" +
                "GROUP BY sm.starId, s.name \n" +
                ")\n" +
                "\n" +
                "\n" +
                "SELECT m.id, m.title, m.year, m.director, r.rating, \n" +
                "(SELECT substring_index(group_concat(DISTINCT CONCAT(g.id, ':', g.name) ORDER BY g.name), ',', 3) \n" +
                "FROM genres_in_movies gm\n" +
                "JOIN genres g ON gm.movieId = m.id AND gm.genreId = g.id) genres,\n" +
                "(SELECT substring_index(GROUP_CONCAT(DISTINCT CONCAT(sr.starId, ':', sr.name) ORDER BY sr.count DESC SEPARATOR ',' ), ',', 3)\n" +
                "FROM StarsRanked sr \n" +
                "JOIN stars_in_movies sm ON sr.starId = sm.starId AND sm.movieId = m.id \n" +
                "GROUP BY sm.movieId \n" +
                "LIMIT 3) stars\n" +
                "FROM MoviesFiltered m\n" +
                "LEFT JOIN ratings r ON m.id = r.movieId;";
    }
}
