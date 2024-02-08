package edu.uci.ics.fabflixmobile.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.ui.movielist.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView message;

    /*
      In Android, localhost is the address of the device or the emulator.
      To connect to your machine, you need to use the below IP address
     */
    private final String host = "10.0.2.2";
    private final String port = "8000";
    private final String domain = "cha-movies";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());

        username = binding.username;
        password = binding.password;
        message = binding.message;
        final Button loginButton = binding.login;

        //assign a listener to call a function to handle the user request when clicking a button
        loginButton.setOnClickListener(view -> login());
    }

    @SuppressLint("SetTextI18n")
    public void login() {
//        message.setText("Trying to login");
        // use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // request type is POST

        final StringRequest loginRequest = new StringRequest(
                Request.Method.POST,
                baseURL + "/api/login",
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.optString("status", "fail");

                        if(status.equals("success")){
                            // Handle successful login
                            message.setText("Login success");
                            Log.d("login.success", response);
                            // Navigate to MovieListActivity
                            Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainPage);
                            finish();
                        }
                        else{
                            message.setText("Login failed");
                            Log.d("login.fail", response);
                        }
                    }
                    catch(JSONException e){
                        message.setText("Error parsing");
                        Log.e("login.error", "Error parsing JSON response", e);
                    }

                },
                error -> {
                    // error
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("login.error", "Server Error: " + responseBody);
                        message.setText("Login failed");
                        // You can also update the UI to reflect the specific error if appropriate
                    } else {
                        message.setText("Error");
                        Log.d("login.error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST request form data
                final Map<String, String> params = new HashMap<>();
                params.put("g-recaptcha-response", "");
                params.put("email", username.getText().toString());
                params.put("password", password.getText().toString());
                params.put("android", "true");
                return params;
            }
        };
        // important: queue.add is where the login request is actually sent

        queue.add(loginRequest);
    }
}
