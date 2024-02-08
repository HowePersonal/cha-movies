package Insertions;


import Entity.User;
import com.google.gson.JsonObject;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Declaring a WebServlet called Browse.SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "AddStarServlet", urlPatterns = "/api/addstar")
public class AddStar extends HttpServlet {
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
        String param_birth_year = request.getParameter("birthYear");
        String param_star_name = request.getParameter("name");

        JsonObject responseJsonObject = new JsonObject();

        if (param_star_name.isEmpty()){
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "fail");
            response.setStatus(500);
            out.write(responseJsonObject.toString());
        }
        else{
            // Get a connection from dataSource and let resource manager close the connection after usage.
            try (out; Connection conn = dataSource.getConnection()) {
                if(param_birth_year.isEmpty()){
                    param_birth_year =null;
                }

                PreparedStatement statement = conn.prepareStatement("insert into stars (id, name, birthYear)\n" +
                        "select concat(SUBSTRING(max(id), 1, 2), CAST(SUBSTRING(max(id), 3) AS UNSIGNED) + 1), ?, ?\n" +
                        "from stars;");

                statement.setString(1, param_star_name);
                statement.setString(2, param_birth_year);

                int rowsAffected = statement.executeUpdate();


                if (rowsAffected > 0) {

                    statement = conn.prepareStatement("select max(id) as max_id from stars;");
                    ResultSet rs = statement.executeQuery();

                    if(rs.next()){

                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("message", "success:starid:" + rs.getString("max_id"));
                        response.setStatus(200);
                    }
                    else{

                        responseJsonObject.addProperty("status", "fail");
                        responseJsonObject.addProperty("message", "fail");
                        response.setStatus(500);
                    }
                } else {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "fail");
                    response.setStatus(500);
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