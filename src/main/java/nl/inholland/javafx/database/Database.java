package nl.inholland.javafx.database;

import nl.inholland.javafx.models.*;
import nl.inholland.javafx.models.csv.CSVParser;
import nl.inholland.javafx.models.login.User;
import nl.inholland.javafx.models.login.UserType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> usersList;
    private List<Room> rooms;
    private List<Movie> allMovies;


    public Database() {
        //Adds the users to the list
        usersList = seedUsers();

        //gets all the seed movies and adds it to a list
        allMovies = seedMovies();

        //Adds the movies to the first list of movies for room 1
        rooms = seedRooms();

        //Gets the local datetime for showings
        LocalDateTime lt = LocalDateTime.now();
        //Seeds the showings for room 1
        seedShowingsForRoom1(lt);

        //Seeds the showings for room 2
        seedShowingsForRoom2(lt);
    }

    public List<User> seedUsers() {
        List<User> users = new ArrayList();
        users.add(new User("ruben123", "Wachtwoord123", UserType.User));
        users.add(new User("wim123", "Password123", UserType.User));
        users.add(new User("mark123", "Welkom123", UserType.Admin));
        return users;
    }



    private void seedShowingsForRoom1(LocalDateTime lt) {
        List<Showing> showingList = new ArrayList<>();
        showingList.add(new Showing(rooms.get(0), allMovies.get(0),
                LocalDateTime.of(lt.getYear(), lt.getMonth(), lt.getDayOfMonth(), 20, 0)
                ));
        showingList.add(new Showing(rooms.get(0), allMovies.get(1),
                LocalDateTime.of(lt.getYear(), lt.getMonth(), lt.getDayOfMonth(), 22, 30)
        ));
        rooms.get(0).setShowings(showingList);
    }

    private void seedShowingsForRoom2(LocalDateTime lt) {
        List<Showing> showingList = new ArrayList<>();
        showingList.add(new Showing(rooms.get(1), allMovies.get(1),
                LocalDateTime.of(lt.getYear(), lt.getMonth(), lt.getDayOfMonth(), 20, 0)
        ));
        showingList.add(new Showing(rooms.get(1), allMovies.get(0),
                LocalDateTime.of(lt.getYear(), lt.getMonth(), lt.getDayOfMonth(), 22, 00)
        ));
        rooms.get(1).setShowings(showingList);
    }

    private List<Room> seedRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(1, 200));
        roomList.add(new Room(2, 100));
        return roomList;
    }

    private List<Movie> seedMovies() {
        //LOADS THE MOVIES FROM A CSV FILE
        CSVParser csvParser = new CSVParser();
        return csvParser.parseMovie();
    }

    public List<User> getUsers() {
        return usersList;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Movie> getAllMovies() {return allMovies; };
}
