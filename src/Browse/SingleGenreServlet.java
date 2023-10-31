package Browse;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import Entity.Movie;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "SingleGenreServlet", urlPatterns = "/api/single-genre")
public class SingleGenreServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> transformResponseToMovies(ResultSet resultSet) throws Exception {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            String movie_id = resultSet.getString("id");
            String movie_title = resultSet.getString("title");
            String movie_year = resultSet.getString("year");
            String movie_director = resultSet.getString("director");
            String movie_rating = resultSet.getString("rating");
            String movie_stars = resultSet.getString("stars");
            String movie_genre = resultSet.getString("genres");

            Movie movie = new Movie(movie_id, movie_title, movie_year, movie_director, movie_rating, movie_stars, movie_genre);
            movies.add(movie);
        }

        return movies;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type


        PrintWriter out = response.getWriter();


        try {

            // Create a new connection to database
            Connection dbCon = dataSource.getConnection();


            String param_id = request.getParameter("id");

            // Generate a SQL query
            String query = "SELECT m.id, m.title, m.director, m.year, r.rating, \n" +
                    "substring_index(group_concat(DISTINCT CONCAT(s.id, ':', s.name) order by num_movies.num_movies desc, s.name), ',', 3) AS stars,\n" +
                    "group_concat(distinct CONCAT(gim.genreId, ':', g.name) order by g.name) genres\n" +
                    "FROM (SELECT movieId FROM genres_in_movies WHERE genreId = ?) gm \n" +
                    "JOIN movies m ON m.id = gm.movieId\n" +
                    "JOIN ratings r ON m.id = r.movieId\n" +
                    "JOIN genres_in_movies gim ON gim.movieId = gm.movieId\n" +
                    "JOIN stars_in_movies sm ON m.id = sm.movieId\n" +
                    "JOIN stars s ON s.id = sm.starId\n" +
                    "JOIN genres g ON gim.genreId = g.id\n" +
                    "JOIN (select s.id as id, s.name as name, count(distinct m.id) as num_movies \n" +
                    "    from stars s \n" +
                    "    join stars_in_movies sm on s.id = sm.starId \n" +
                    "    join movies m on sm.movieId = m.id \n" +
                    "    group by s.id, s.name) num_movies on num_movies.id = s.id \n" +
                    "GROUP BY m.id, m.title, m.director, m.year, r.rating\n" +
                    "order by r.rating DESC;";




            PreparedStatement statement = dbCon.prepareStatement(query);
            statement.setString(1, param_id);


            request.getServletContext().log("query：" + query);

            ResultSet resultSet = statement.executeQuery();
            List<Movie> movies = transformResponseToMovies(resultSet);

            JsonArray jsonArray = new JsonArray();

            for (Movie m: movies) {
                jsonArray.add(m.movieToJson());
            }


            // Close all structures
            resultSet.close();
            statement.close();
            dbCon.close();

            // Log to localhost log
            request.getServletContext().log("returning " + jsonArray.size() + " movies");

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);



        } catch (Exception e) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            response.setStatus(500);
        }
        finally {
            out.close();
        }
    }
}
