package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovieBinding;
import org.json.JSONException;
import org.json.JSONObject;


public class MovieActivity extends AppCompatActivity {

    private final String host = "10.0.2.2";
    private final String port = "8000";
    private final String domain = "cha-movies";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;

    private TextView title;
    private TextView year;
    private TextView director;
    private TextView genres;
    private TextView stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieBinding binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent movieList = getIntent();
        String id = movieList.getStringExtra("id");

        title = binding.title;
        year = binding.subtitle;
        director = binding.subtitle1;
        genres = binding.subtitle2;
        stars = binding.subtitle3;

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        Log.d("id",id);
        final JsonArrayRequest  jsonResponse = new JsonArrayRequest (
                Request.Method.GET,
                baseURL + "/api/single-movie?id="+id,
                null,
                response -> {
                    try{
                        JSONObject movieObject  = response.getJSONObject(0);

                        String movie_title = movieObject.optString("movie_title", "");
                        String movie_year = movieObject.optString("movie_year", "");
                        String movie_director = movieObject.optString("movie_director", "");
                        String movie_stars = movieObject.optString("movie_stars", "");
                        String movie_genres = movieObject.optString("movie_genres", "");

                        String[] split_genres = movie_genres.split(",");
                        String out_genre = "";
                        for(String genre : split_genres){
                            String[] keyValue = genre.split(":");
                            if(out_genre.isEmpty()){
                                out_genre = keyValue[1] + ",";
                            }
                            else{
                                out_genre = out_genre.concat(keyValue[1] + ",");
                            }
                        }
                        out_genre = out_genre.substring(0, out_genre.length()-1);

                        String out_star = "";
                        if(!movie_stars.equals("null")){
//                                Log.d("ArrayListExample", movie_stars);
                            String[] split_stars = movie_stars.split(",");
                            for(String star : split_stars){
                                String[] keyValue = star.split(":");
                                if(out_star.isEmpty()){
                                    out_star = keyValue[1] + ",";
                                }
                                else{
                                    out_star = out_star.concat(keyValue[1] + ",");
                                }
                            }
                            out_star = out_star.substring(0, out_star.length()-1);
                        }

                        title.setText(movie_title);
                        director.setText(movie_director);
                        year.setText(movie_year);
                        genres.setText(out_genre);
                        stars.setText(out_star);
                    }
                    catch(JSONException e){
                        Log.e("MovieActivity", "Error parsing response", e);
                    }

                },
                error -> {
                    Log.e("Single movie", "Server unresponsive");
                    error.printStackTrace();
                }
        );
        queue.add(jsonResponse);
    }

}