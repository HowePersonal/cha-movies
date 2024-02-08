package Entities;

import java.util.ArrayList;

public class Star {
    private String id;
    private String name;
    private String birthYear;
    private ArrayList<String> movies;

    public Star(String id, String name, String birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.movies = new ArrayList<>();
    }

    public ArrayList<String> getMovies() {return movies;}

    public void addMovie(String movieId) {movies.add(movieId);}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

}
