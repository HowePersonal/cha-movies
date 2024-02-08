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
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import android.content.Intent;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import org.json.JSONException;
import org.json.JSONObject;


public class MovieListActivity extends AppCompatActivity {

    private double maxPage;
    private TextView pageNum;
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final String host = "10.0.2.2";
    private final String port = "8000";
    private final String domain = "cha-movies";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        Intent search = getIntent();
        String title = search.getStringExtra("title");
        Log.d("title", title );

        ActivityMovielistBinding binding = ActivityMovielistBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());
        pageNum = binding.pageNumber;

        final Button previous = binding.prevButton;
        final Button next = binding.nextButton;

        previous.setOnClickListener(view -> prev());
        next.setOnClickListener(view -> next());
        pageNum.setText("1");
        // TODO: this should be retrieved from the backend server


        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                baseURL + "/api/search?title=" + title,
                null,
                response -> {
                    try{
                        maxPage = Math.floor(response.length() / 10) + 1;
                        for(int i = 0; i < response.length(); i++){
                            JSONObject movieObject  = response.getJSONObject(i);
                            String movie_id = movieObject.optString("movie_id", "");
                            String movie_title = movieObject.optString("movie_title", "");
                            String movie_year = movieObject.optString("movie_year", "");
                            String movie_director = movieObject.optString("movie_director", "");
                            String movie_stars = movieObject.optString("movie_stars", "");
                            String movie_genres = movieObject.optString("movie_genres", "");

                            String out_genre = "";
                            if(!movie_genres.equals("null")) {
                                String[] genres = movie_genres.split(",");
                                for(String genre : genres){
                                    if (genre.contains(":")) {
                                        String[] keyValue = genre.split(":");
                                        if (keyValue.length > 1) {
                                            out_genre = out_genre.concat(keyValue[1] + ",");
                                        }
                                    }
                                }
                                if (!out_genre.isEmpty()) {
                                    out_genre = out_genre.substring(0, out_genre.length() - 1);
                                }
                            }


                            String out_star = "";
                            if(!movie_stars.equals("null")){
                                String[] stars = movie_stars.split(",");
                                for(String star : stars){
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
                            movies.add(new Movie(movie_id, movie_title, movie_year, movie_director, out_genre, out_star));
                        }

                        updateListView(1);
                    }
                    catch (JSONException e){
                        Log.e("movie.error", "Error parsing JSON response", e);
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("movie.error", "Server Error: " + responseBody);
                    }
                    else{
                        Log.e("movie.error", "Error server unresponsive");
                        error.printStackTrace();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    @SuppressLint("SetTextI18n")
    public void prev() {
        int curPage = Integer.parseInt(pageNum.getText().toString());
        if(curPage > 1){
            curPage -= 1;
            pageNum.setText(String.valueOf(curPage));
            updateListView(curPage);
        }
    }

    @SuppressLint("SetTextI18n")
    public void next() {
        int curPage = Integer.parseInt(pageNum.getText().toString());
        if(curPage < maxPage){
            curPage += 1;
            pageNum.setText(String.valueOf(curPage));
            updateListView(curPage);
        }
    }

    private void updateListView(int curPage){
        ArrayList<Movie> moviesList = new ArrayList<>();

        int startIndex = (curPage-1) * 10;
        int endIndex = Math.min(startIndex + 10, movies.size());

        for(int i = startIndex; i < endIndex; i++){
            moviesList.add(movies.get(i));
        }

        MovieListViewAdapter adapter = new MovieListViewAdapter(this, moviesList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = moviesList.get(position);
            @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %s, %s", position, movie.getTitle(), movie.getYear());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Intent singleMoviePage = new Intent(MovieListActivity.this, MovieActivity.class);
            singleMoviePage.putExtra("id", movie.getId());
            startActivity(singleMoviePage);
        });
    }
}