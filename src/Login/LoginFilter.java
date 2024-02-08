package Login;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    private final ArrayList<String> allowedURIs = new ArrayList<>();
    private final ArrayList<String> adminRequiredURIs = new ArrayList<>();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession();

        if (doesUrlRequireAdmin(httpRequest.getRequestURI())) {
            if (session.getAttribute("admin") == null) {
                httpResponse.sendRedirect("/cha-movies/elogin");
            }
            else {
                chain.doFilter(request, response);
            }
            return;
        }

        // Check if this URL is allowed to access without logging in
        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
            // Keep default action: pass along the filter chain
            chain.doFilter(request, response);
            return;
        }

        // Redirect to login page if the "user" attribute doesn't exist in session

        if (session.getAttribute("user") == null) {
            httpResponse.sendRedirect("/cha-movies/login");
        } else {
            chain.doFilter(request, response);
        }
    }
    private boolean doesUrlRequireAdmin(String requestURI) {
        return adminRequiredURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }
    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        return allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }

    public void init(FilterConfig fConfig) {
        adminRequiredURIs.add("/cha-movies/_dashboard");

        allowedURIs.add("Login");
        allowedURIs.add("/cha-movies/assets/index-196b0280.js");
        allowedURIs.add("/cha-movies/vite.svg");
        allowedURIs.add("/cha-movies/api/login");
        allowedURIs.add("/cha-movies/api/signup");
        allowedURIs.add("/cha-movies/api/title");
        allowedURIs.add("/cha-movies/api/genre");

    }


}