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
import Util.SQLQueries;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called FormServlet, which maps to url "/form"
@WebServlet(name = "Search", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {

    long elapsedTimeTJ = 0;
    long startTime;
    long endTime;
    SQLQueries sqlQueries = new SQLQueries();
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
        elapsedTimeTJ = 0;
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        startTime = System.nanoTime();
        try (out; Connection dbCon = dataSource.getConnection()) {
            endTime = System.nanoTime();
            elapsedTimeTJ += endTime - startTime;
            // Create a new connection to database


            String param_title = request.getParameter("title");

            String prefix_title = param_title + " ";
            prefix_title = prefix_title.replaceAll("\\s+", "* ");


            startTime = System.nanoTime();
            String AutocompleteQuery = sqlQueries.SEARCH_QUERY("18446744073709551615");
            PreparedStatement statement = dbCon.prepareStatement(AutocompleteQuery);
            statement.setString(1, prefix_title);

            ResultSet rs = statement.executeQuery();
            endTime = System.nanoTime();
            elapsedTimeTJ += endTime - startTime;

            JsonArray jsonArray = new JsonArray();
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
            startTime = System.nanoTime();
            rs.close();
            statement.close();
            dbCon.close();
            endTime = System.nanoTime();
            elapsedTimeTJ += endTime - startTime;

            out.write(jsonArray.toString());

            response.setStatus(200);
        } catch (Exception e) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        }
        finally {
            out.close();
            FileWriter file = new FileWriter("F:\\School\\CS-122B\\cha-movies\\time-log\\timeTJ.txt", true);
            file.write(String.valueOf(elapsedTimeTJ)+"\n");
            file.close();
        }
    }
}