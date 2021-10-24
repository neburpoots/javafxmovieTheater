package nl.inholland.javafx.models.login;

import nl.inholland.javafx.database.Database;
import nl.inholland.javafx.models.MovieTheater;
import nl.inholland.javafx.views.windows.MainWindow;

import java.util.List;

public class Login {
    private Database db;
    private List<User> usersList;

    private String errorMessage;

    public Login() {
        db = new Database();
        usersList = db.getUsers();
    }

    public boolean CheckLogin(String username, String password, MovieTheater movieTheater) {
        for(User u: usersList) {
            if(u.getUserName().equals(username)) {

                if(u.getPassword().equals(password)) {
                    MainWindow mw = new MainWindow(u, movieTheater);
                    mw.getStage().show();

                    return true;
                }
            }
        }
        errorMessage = "Wrong login credentials";
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
