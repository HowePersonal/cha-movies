package Cart;


import Entity.User;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Declaring a WebServlet called Browse.SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SaleServlet", urlPatterns = "/api/payment/confirmation")
public class SaleServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/master");
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
        Integer param_sale_id = Integer.valueOf(request.getParameter("sale_id"));
        String param_movie_name = request.getParameter("movie_id");


        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (out; Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"
            HttpSession session = request.getSession();

            Object userObj = session.getAttribute("user");
            User currUser = (User) userObj;

            String user_email = currUser.getEmail();
            String user_password = currUser.getPassword();

            String query = String.format("SELECT id FROM customers WHERE email = '%s' AND password = '%s'", user_email, user_password);
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery(query);

            Integer user_id = 0;
            if (!rs.next() == false) {
                user_id = Integer.valueOf(rs.getString("id"));
            }

            LocalDate today = LocalDate.now();
            String formattedDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

            query = String.format("SELECT id FROM movies WHERE title = '%s'", param_movie_name);
            statement = conn.prepareStatement(query);
            rs = statement.executeQuery(query);

            String param_movie_id = "";
            if (!rs.next() == false) {
                param_movie_id = rs.getString("id");
            }

            query = String.format("INSERT IGNORE INTO sales (id, customerId, movieId, saleDate)\n" +
                    "VALUES(%d,%d,'%s','%s')", param_sale_id, user_id, param_movie_id, formattedDate);

            // Declare our statement
            System.out.println(query);
            statement = conn.prepareStatement(query);
            statement.executeUpdate(query);


            // Iterate through each row of rs
            statement.close();


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

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }

}