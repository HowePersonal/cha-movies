package Search;

import javax.naming.InitialContext;
import javax.naming.NamingException;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "AdvancedSearchServlet", urlPatterns = "/api/advancedsearch")
public class AdvancedSearchServlet extends HttpServlet {

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // Use http GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

//        // Building page head with title
//        out.println("<html><head><title>MovieDBExample: Found Records</title></head>");
//
//        // Building page body
//        out.println("<body><h1>MovieDBExample: Found Records</h1>");


        try (out; Connection dbCon = dataSource.getConnection()) {


            // Declare a new statement


            // Retrieve parameter "name" from the http request, which refers to the value of <input name="name"> in index.html
            String stars = request.getParameter("stars");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String title = request.getParameter("title");

            // Generate a SQL query
            String query = String.format("WITH MoviesFiltered AS (\n" +
                    "    SELECT DISTINCT * FROM movies m GROUP BY m.id \n" +
                    "    HAVING m.director LIKE '%1$s%4$s%1$s' AND m.title LIKE '%1$s%5$s%1$s' \n" +
                    "    AND ELT(if('%3$s' = '', 2, 1), m.year = '%3$s', m.year = min(m.year) < m.year < min(m.year)) \n" +
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
                    "LEFT JOIN ratings r ON m.id = r.movieId\n" +
                    "having stars like '%1$s%2$s%1$s';","%", stars, year,director, title);

            // Log to localhost log
            request.getServletContext().log("query：" + query);
            PreparedStatement statement = dbCon.prepareStatement(query);
            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");
                String movie_stars = rs.getString("stars");
                String movie_genres = rs.getString("genres");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("movie_stars", movie_stars);
                jsonObject.addProperty("movie_genres", movie_genres);

                jsonArray.add(jsonObject);
            }

            // Close all structures
            rs.close();
            statement.close();
            dbCon.close();

            // Log to localhost log
            request.getServletContext().log("searching " + jsonArray.size() + " results");

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);



        } catch (Exception e) {
            /*
             * After you deploy the WAR file through tomcat manager webpage,
             *   there's no console to see the print messages.
             * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
             *
             * To view the last n lines (for example, 100 lines) of messages you can use:
             *   tail -100 catalina.out
             * This can help you debug your program after deploying it on AWS.
             */
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
//            request.getServletContext().log("Error: ", e);
//
//            // Output Error Massage to html
//            out.println(String.format("<html><head><title>MovieDBExample: Error</title></head>\n<body><p>SQL error in doGet: %s</p></body></html>", e.getMessage()));
//            return;
            response.setStatus(500);
        }
        finally {
            out.close();
        }
    }
}