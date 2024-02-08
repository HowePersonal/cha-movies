package Cart;

import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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

// Declaring a WebServlet called Browse.SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class PaymentServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();





        // Retrieve parameter movie id from url request.
        String param_credit_num = request.getParameter("credit_num");
        String param_first_name = request.getParameter("first_name");
        String param_last_name = request.getParameter("last_name");
        String param_expiration_date = request.getParameter("expiration_date");


        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (out; Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource

            // Construct a query with parameter represented by "?"

            String query = "SELECT * FROM creditcards \n" +
                    "WHERE id = ? AND firstName = ? AND \n" +
                    "lastName = ? AND expiration = ?;";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, param_credit_num);
            statement.setString(2, param_first_name);
            statement.setString(3, param_last_name);
            statement.setString(4, param_expiration_date);
            ResultSet rs = statement.executeQuery();


            JsonObject jsonObject = new JsonObject();

            // Iterate through each row of rs

            if (rs.next()) {
                jsonObject.addProperty("status", "success");
                response.setStatus(200);
            }
            else {
                jsonObject.addProperty("status", "fail");
                response.setStatus(401);
            }

            rs.close();
            statement.close();
            out.write(jsonObject.toString());


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