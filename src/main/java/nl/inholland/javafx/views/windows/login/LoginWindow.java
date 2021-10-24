package nl.inholland.javafx.views.windows.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.inholland.javafx.models.MovieTheater;
import nl.inholland.javafx.models.login.Login;
import nl.inholland.javafx.models.login.User;
import nl.inholland.javafx.views.StyledScene;
import nl.inholland.javafx.views.windows.MainWindow;
import nl.inholland.javafx.views.windows.components.ManageMovies;
import nl.inholland.javafx.views.windows.components.Title;
import nl.inholland.javafx.views.windows.components.forms.AddTicketForm;
import org.w3c.dom.Text;

public class LoginWindow {
    private MovieTheater movieTheater;
    private Scene scene;
    private Stage stage;
    private Login login;

    private GridPane gridPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label validationError;

    public Stage getStage() {
        return stage;
    }

    public LoginWindow() {
        this.movieTheater = new MovieTheater();
        setUpLoginWindow();
    }

    //For the logout
    public LoginWindow(MovieTheater movieTheater) {
        this.movieTheater = movieTheater;
        setUpLoginWindow();
    }

    public void setUpLoginWindow() {
        this.login = new Login();
        this.stage = new Stage();
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(50);
        gridPane.setVgap(30);
        gridPane.getStyleClass().add("Form");
        gridPane.getStyleClass().add("globalLayout");
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        makeLoginForm();
        VBox layout = new VBox();

        layout.getChildren().add(gridPane);
        layout.getStyleClass().add("Form");
        layout.getStyleClass().add("globalLayout");
        scene = new StyledScene(layout);
        scene.widthProperty();
        stage.setTitle("Fabulous Cinema Login");
        stage.setScene(scene);
    }

    private void makeLoginForm() {
        Title title = new Title("Login");
        title.getStyleClass().add("title");
        Label userNameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");

        usernameField = new TextField();
        usernameField.setPromptText("Your username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Your password");

        gridPane.add(title, 0,0);
        gridPane.add(userNameLabel, 0, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordField, 1, 2);

        Button loginBTN = new Button("Login");
        loginBTN.setMinWidth(150);
        gridPane.add(loginBTN, 0, 3);
        loginBTN.setOnAction(setLoginBTN());

        validationError = new Label();
        gridPane.add(validationError, 1, 3);

    }

    private EventHandler setLoginBTN() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkLogin();
            }
        };
        return buttonHandler;
    }

    private void checkLogin() {
        gridPane.getStyleClass().clear();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(login.CheckLogin(username, password, movieTheater)) {
            stage.close();
        } else {
            gridPane.getStyleClass().add("wrongInput");
            validationError.setText(login.getErrorMessage());
        }
    }

}
