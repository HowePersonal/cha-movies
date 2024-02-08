package Browse;

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
import java.sql.Statement;

@WebServlet(name = "GenreServlet", urlPatterns = "/api/genre")
public class GenreServlet extends HttpServlet {

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

        PrintWriter out = response.getWriter();

        try (out; Connection dbCon = dataSource.getConnection()) {


            PreparedStatement statement = dbCon.prepareStatement("SELECT DISTINCT id, name genre from genres ORDER BY name;");



            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();


            while (rs.next()) {
                        String genre = rs.getString("genre");
                long id = rs.getLong("id");


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("genre", genre);
                jsonObject.addProperty("id", id);
                jsonArray.add(jsonObject);
            }


            rs.close();
            statement.close();
            dbCon.close();


            request.getServletContext().log("returning " + jsonArray.size() + " genres");


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
        }
    }
}
