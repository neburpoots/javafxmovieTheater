package nl.inholland.javafx.models;

import nl.inholland.javafx.database.Database;
import nl.inholland.javafx.models.login.User;

import java.util.List;

public class MovieTheater {
    private Database db;
    private List<Room> rooms;
    private List<Movie> allMovies;

    private User user;

    public MovieTheater() {
        this.db = new Database();
        rooms = db.getRooms();
        allMovies = db.getAllMovies();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }

    public void addMovie(Movie movie) {
        allMovies.add(movie);
    }
}
