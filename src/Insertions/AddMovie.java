package Insertions;


import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called Browse.SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "AddMovieServlet", urlPatterns = "/api/addmovie")
public class AddMovie extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();


        // Retrieve parameter movie id from url request.
        String param_movie_title = request.getParameter("title");
        String param_movie_year = request.getParameter("year");
        String param_movie_director = request.getParameter("director");
        String param_birth_year = request.getParameter("birthYear");
        String param_star_name = request.getParameter("name");
        String param_genre = request.getParameter("genre");

        JsonObject responseJsonObject = new JsonObject();



        if (param_star_name.isEmpty() || param_genre.isEmpty() ||
                param_movie_title.isEmpty() || param_movie_director.isEmpty() ||
                param_movie_year.isEmpty()){
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "fail");
            response.setStatus(500);
            out.write(responseJsonObject.toString());
        }
        else{
            if(param_birth_year.isEmpty()){
                param_birth_year = null;
            }
            // Get a connection from dataSource and let resource manager close the connection after usage.
            try (out; Connection conn = dataSource.getConnection()) {


                // Declare our statement
//                System.out.println(query);
                PreparedStatement statement = conn.prepareStatement("call add_movie(?,?,?,?,?,?)");

                statement.setString(1,param_movie_title);
                statement.setString(2,param_movie_year);
                statement.setString(3,param_movie_director);
                statement.setString(4,param_star_name);
                statement.setString(5,param_birth_year);
                statement.setString(6,param_genre);

//                int rowsAffected = statement.executeUpdate();
                ResultSet rs = statement.executeQuery();
//                boolean hasResults = statement.execute();
//                System.out.println(hasResults);
                if(rs.next()) {
                    if (!rs.getString("status").equals("Duplicate")) {
                        String new_movie_id = rs.getString("new_movie_id");
                        String new_star_id = rs.getString("new_star_id");
                        String new_genre_id = rs.getString("new_genre_id");
                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success:" + "movieid:" + new_movie_id + "starid:" + new_star_id + "genreid:" + new_genre_id);
                        response.setStatus(200);
                    } else {
                        responseJsonObject.addProperty("status", "in database");
                        responseJsonObject.addProperty("message", "Duplicate movie");
                        response.setStatus(500);
                    }
                }
                // Iterate through each row of rs
                statement.close();
                out.write(responseJsonObject.toString());



            } catch (Exception e) {
                // Write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", e.getMessage());
                out.write(jsonObject.toString());

                // Log error to localhost log
                request.getServletContext().log("Error:", e);
                // Set response status to 500 (Internal Server Error)
                response.setStatus(500);
            } finally {
                out.close();
            }
        }
        



        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}