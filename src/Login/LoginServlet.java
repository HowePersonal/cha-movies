package Login;

import Entity.User;
import com.google.gson.JsonObject;
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
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private DataSource dataSource;
    private StrongPasswordEncryptor strongPasswordEncryptor = new StrongPasswordEncryptor();

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String android = request.getParameter("android");
        boolean bypass = false;
        if(android != null && android.equals("true")){
            bypass = true;
        }

        System.out.println("Username: " + email + " Password: " + password + " Android: " + android);
        JsonObject responseJsonObject = new JsonObject();
        PrintWriter out = response.getWriter();

        if(!bypass){
            try {
                RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            } catch (Exception e) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", "reCaptcha not satisfied");
                out.write(jsonObject.toString());
                response.setStatus(500);
                return;
            }
        }

        try (Connection conn = dataSource.getConnection()) {

            String query = "SELECT * FROM customers WHERE email = ?";


            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next() == false) {
                String encryptedPassword = rs.getString("password");
                if (strongPasswordEncryptor.checkPassword(password, encryptedPassword)) {
                    HttpSession session = request.getSession();

                    session.setAttribute("user", new User(email, password));

                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    response.setStatus(200);
                }
                else {
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("errorMessage", "Wrong password information");
                    request.getServletContext().log("Login failed");
                    response.setStatus(500);
                }
            } else {
                // Login fail
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("errorMessage", "Wrong email information");
                request.getServletContext().log("Login failed");
                response.setStatus(500);
            }
            rs.close();
            statement.close();
            out.write(responseJsonObject.toString());
        } catch (Exception e) {

            // Write error message JSON object to output
            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}