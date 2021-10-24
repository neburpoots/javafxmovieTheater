package nl.inholland.javafx.views.windows.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import nl.inholland.javafx.models.ComponentEnum;
import nl.inholland.javafx.models.MovieTheater;
import nl.inholland.javafx.models.login.User;
import nl.inholland.javafx.models.login.UserType;
import nl.inholland.javafx.views.windows.MainWindow;
import nl.inholland.javafx.views.windows.login.LoginWindow;

public class Navigation extends HBox {

    private User user;

    private MainWindow mainWindow;
    private MovieTheater movieTheater;

    private MenuItem manageShowings;
    private MenuItem manageMovies;
    private MenuItem addTicketsButton;
    private MenuItem logoutItemButton;

    public Navigation(MainWindow mainWindow, MovieTheater movieTheater, User user) {
        this.mainWindow = mainWindow;
        this.movieTheater = movieTheater;

        this.setPadding(new Insets(10,10,10,10));
        this.setSpacing(10);
        this.getStyleClass().add("navigation");

        MenuButton ticketsButton = new MenuButton("Tickets");
        addTicketsButton = new MenuItem("Purchase tickets");
        ticketsButton.getItems().addAll(addTicketsButton);
        ticketsButton.setMinWidth(150);

        if(user.getUserType() == UserType.Admin) {
            MenuButton adminButton = new MenuButton("Admin");
            manageShowings = new MenuItem("Manage showings");
            manageMovies = new MenuItem("Manage movies");
            adminButton.getItems().addAll(manageShowings, manageMovies);
            adminButton.setMinWidth(150);
            setAdminMenuButtons();
            this.getChildren().add(adminButton);

        }

        MenuButton helpButton = new MenuButton("Help");
        helpButton.getItems().addAll(new MenuItem("About"));
        helpButton.setMinWidth(150);

        MenuButton logoutButton = new MenuButton("Logout");
        logoutItemButton = new MenuItem("Logout...");
        logoutButton.getItems().addAll(logoutItemButton);
        logoutButton.setMinWidth(150);

        setStandardMenuButtons();
        this.getChildren().addAll(ticketsButton, helpButton, logoutButton);
    }

    public void updateNavigation(MainWindow mainWindow, MovieTheater movieTheater) {
        this.mainWindow = mainWindow;
        this.movieTheater = movieTheater;
    }

    private void setAdminMenuButtons() {
        manageShowings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updatePage(ComponentEnum.AddShowingForm);
            }
        });
        manageMovies.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updatePage(ComponentEnum.ManageMovies);
            }
        });
    }

    private void setStandardMenuButtons() {
        addTicketsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updatePage(ComponentEnum.AddTicketForm);
            }
        });
        logoutItemButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoginWindow lw = new LoginWindow(movieTheater);
                mainWindow.closeForm();
                lw.getStage().show();
            }
        });
    }

    public void updatePage(ComponentEnum componentEnum) {
        mainWindow.changeComponents(componentEnum);
    }
}
