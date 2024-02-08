import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;

import Entities.Movie;
import Entities.Star;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {

    private HashMap<String, Integer> genreMap;
    private Statement statement;

    private HikariDataSource dataSource;
    private ResultSet resultSet;

    private Connection conn;


    public Database() throws Exception {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/moviedb?rewriteBatchedStatements=true");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("mytestuser");
        config.setPassword("My6$Password");

        dataSource = new HikariDataSource(config);

        genreMap = new HashMap<>();

        try {
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement = conn.createStatement();


            String query = "SELECT DISTINCT * from genres;";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                genreMap.put(resultSet.getString("name"), resultSet.getInt("id"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getLastMovieID() throws Exception {
        int movieId = 0;
        try {
            statement = conn.createStatement();
            String query = "select max(id) id from movies;";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                movieId = Integer.parseInt(id.replaceAll("[^0-9]", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieId;
    }

    public int getLastActorID() throws Exception {
        int actorId = 0;
        try  {
            statement = conn.createStatement();
            String query = "select max(id) id from stars;";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                actorId = Integer.parseInt(id.replaceAll("[^0-9]", ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorId;
    }

    public int getLastGenreId() throws Exception {
        int genreId = 0;
        try  {
            statement = conn.createStatement();
            String query = "select max(id) id from genres";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String id = resultSet.getString("id");
                genreId = Integer.parseInt(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genreId;
    }

    public void batchInsertActorMovies(Map<String, Star> starMap) throws Exception {
        int starsInMoviesInsertedCount = 0;
        try {
            conn.setAutoCommit(false);

            String starMovieQuery = "insert ignore into stars_in_movies (starId, movieId) values(?, ?)";
            PreparedStatement starMovieStatement = conn.prepareStatement(starMovieQuery);

            int starMapSize = starMap.size();
            int i = 0;
            for (Star s : starMap.values()) {
                i++;

                String actorId = s.getId();
                ArrayList<String> movies = s.getMovies();
                for (int j = 0; j < movies.size(); j++) {
                    starMovieStatement.setString(1, actorId);
                    starMovieStatement.setString(2, movies.get(j));
                    starsInMoviesInsertedCount++;
                    starMovieStatement.addBatch();

                    if (i != 0 && (i % 1000 == 0 || i == starMapSize-1)) {
                        try {
                            starMovieStatement.executeBatch();
                            conn.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Inserted " + starsInMoviesInsertedCount + " stars_in_movies");
    }
    public void batchInsertActors(Map<String, Star> starMap) throws Exception {
        int starsInsertedCount = 0;
        try  {
            conn.setAutoCommit(false);
            String starQuery = "insert ignore into stars (id, name, birthYear) values(?, ?, ?)";
            PreparedStatement starStatement = conn.prepareStatement(starQuery);


            int starMapSize = starMap.size();
            int i = 0;
            for (Star s : starMap.values()) {
                starStatement.setString(1, s.getId());
                starStatement.setString(2, s.getName());
                starStatement.setString(3, s.getBirthYear());
                starStatement.addBatch();
                starsInsertedCount++;
                i++;


                if (i != 0 && (i % 1000 == 0 || i == starMapSize-1)) {
                    try {
                        starStatement.executeBatch();
                        conn.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Inserted " + starsInsertedCount + " stars");
    }
    public void batchInsertMovies(List<Movie> movies) throws Exception {
        int moviesInsertedCount = 0;
        int genresInsertedCount = 0;
        int genresInMoviesCount = 0;

        try {
            conn.setAutoCommit(false);

            String movieQuery = "insert ignore into movies (id, title, year, director) values(?, ?, ?, ?);";
            PreparedStatement movieStatement = conn.prepareStatement(movieQuery);

            int genreId = getLastGenreId() + 1;
            String genreQuery = "insert ignore into genres (id, name) values(?, ?)";
            PreparedStatement genreStatement = conn.prepareStatement(genreQuery);

            String genresInMoviesQuery = "insert ignore into genres_in_movies (genreId, movieId) values (?, ?)";
            PreparedStatement genresInMoviesStatement = conn.prepareStatement(genresInMoviesQuery);

            int movieSize = movies.size();
            for (int i = 0; i < movieSize; i++) {
                Movie m = movies.get(i);
                movieStatement.setString(1, m.getId());
                movieStatement.setString(2, m.getTitle());
                movieStatement.setInt(3, Integer.parseInt(m.getYear()));
                movieStatement.setString(4, m.getDirector());
                moviesInsertedCount++;
                movieStatement.addBatch();

                ArrayList<String> genres = m.getGenres();

                for (int j = 0; j < genres.size(); j++) {
                    String genre = genres.get(j).trim();



                    if (!genreMap.containsKey(genre)) {
                        genreStatement.setInt(1, genreId);
                        genreStatement.setString(2, genre);
                        genreStatement.addBatch();
                        genresInMoviesStatement.setInt(1, genreId);
                        genresInMoviesStatement.setString(2, m.getId());
                        genresInMoviesStatement.addBatch();
                        genreMap.put(genre, genreId);
                        genresInMoviesCount++;
                        genreId++;
                        genresInsertedCount++;
                    }
                    else {
                        genresInMoviesStatement.setInt(1, genreMap.get(genre));
                        genresInMoviesStatement.setString(2, m.getId());
                        genresInMoviesStatement.addBatch();
                        genresInMoviesCount++;
                    }
                }


                if (i != 0 && (i % 1000 == 0 || i == movieSize-1)) {
                    try {
                        movieStatement.executeBatch();
                        genreStatement.executeBatch();
                        conn.commit();
                        genresInMoviesStatement.executeBatch();
                        conn.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            movieStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Inserted " + moviesInsertedCount + " movies");
        System.out.println("Inserted " + genresInsertedCount + " genres");
        System.out.println("Inserted " + genresInMoviesCount + " genres_in_movies");
    }


}
