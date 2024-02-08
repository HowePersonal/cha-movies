package Entities;

import java.util.ArrayList;

public class Movie {
    private String id;
    private String title;
    private String year;
    private String director;
    private ArrayList<String> genres;
    public Movie(String id, String title, String year, String director) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.genres = new ArrayList<>();
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }

    public int genreListSize() {
        return this.genres.size();
    }

    public ArrayList<String> getGenres() {
        return this.genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String toString() {
        return "ID: " + getId() + ", " +
                "Title: " + getTitle() + ", " +
                "Year: " + getYear() + ", " +
                "Director: " + getDirector() + "." +
                "Genres: ";
    }

}
