package Cart;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {

    /**
     * handles GET requests to store session information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        long lastAccessTime = session.getLastAccessedTime();

        JsonObject responseJsonObject = new JsonObject();

//        responseJsonObject.addProperty("sessionID", sessionId);
//        responseJsonObject.addProperty("lastAccessTime", new Date(lastAccessTime).toString());

        Map<String, Integer> previousItems = (Map<String, Integer>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new HashMap<String, Integer>();
        }


        for (Map.Entry<String, Integer> i : previousItems.entrySet()) {
            responseJsonObject.addProperty(i.getKey(), String.valueOf(i.getValue()));
        }

        // write all the data into the jsonObject
        response.getWriter().write(responseJsonObject.toString());
    }

    /**
     * handles POST requests to add and show the item list information
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String item = request.getParameter("item");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // get the previous items in a ArrayList
        Map<String, Integer> previousItems = (Map<String, Integer>) session.getAttribute("previousItems");
        if (previousItems == null) {
            previousItems = new HashMap<String, Integer>();
            session.setAttribute("previousItems", previousItems);
        }

        synchronized (previousItems) {
            if (action.equals("purchase") || action.equals("increase")) {
                if (previousItems.containsKey(item)) {
                    previousItems.put(item, previousItems.get(item)+1);
                }
                else {
                    previousItems.put(item, 1);
                }
            }
            else if (action.equals("decrease")) {
                if (previousItems.containsKey(item)) {
                    previousItems.put(item, previousItems.get(item)-1);
                    if (previousItems.get(item) == 0) {
                        previousItems.remove(item);
                    }
                }

            }
            else if (action.equals("delete")) {
                if (previousItems.containsKey(item)) {
                    previousItems.remove(item);
                }
            }
        }



        JsonObject responseJsonObject = new JsonObject();

        for (Map.Entry<String, Integer> i : previousItems.entrySet()) {
            responseJsonObject.addProperty(i.getKey(), String.valueOf(i.getValue()));
        }

        response.getWriter().write(responseJsonObject.toString());
    }
}