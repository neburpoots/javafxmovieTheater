package nl.inholland.javafx.views.windows;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.inholland.javafx.models.ComponentEnum;
import nl.inholland.javafx.models.MovieTheater;
import nl.inholland.javafx.models.Showing;
import nl.inholland.javafx.models.login.User;
import nl.inholland.javafx.views.StyledScene;
import nl.inholland.javafx.views.windows.components.*;
import nl.inholland.javafx.views.windows.components.forms.AddShowingForm;
import nl.inholland.javafx.views.windows.components.forms.AddTicketForm;

public class MainWindow {
    private User user;
    private Scene scene;
    private Stage stage;
    private MovieTheater movieTheater;
    private MainContent mainContent;
    private Navigation navigation;
    private AddShowingForm movieForm;
    private VBox globalLayout;
    private Title title;
    private HBox placeholder;
    private AddTicketForm addTicketForm;
    private ManageMovies manageMovies;

    public Stage getStage() {
        return stage;
    }

    public MainWindow(User currentUser, MovieTheater movieTheater) {
        this.user = currentUser;
        this.movieTheater = movieTheater;
        this.stage = new Stage();
        stage.setMinWidth(1200);
        stage.setMinHeight(800);

        globalLayout = new VBox();
        globalLayout.getStyleClass().add("globalLayout");

        //Initial setup
        navigation = new Navigation(this, movieTheater, user);
        title = new Title("Purchase tickets");
        mainContent = new MainContent(this, movieTheater, ComponentEnum.AddTicketForm);
        placeholder = new HBox();
        globalLayout.getChildren().addAll(navigation, title, mainContent, placeholder);


        scene = new StyledScene(globalLayout);
        scene.widthProperty();
        stage.setTitle("Fabulous Cinema | Purchase tickets");
        stage.setScene(scene);
    }

    public void changeComponents(ComponentEnum componentEnum) {
        navigation.updateNavigation(this, movieTheater);
        switch(componentEnum) {
            case AddShowingForm:
                stage.setTitle("Fabulous Cinema | Manage showings");
                title = new Title("Manage showings");
                mainContent = new MainContent(this, movieTheater, ComponentEnum.AddShowingForm);
                movieForm = new AddShowingForm(movieTheater, this);
                globalLayout.getChildren().set(1, title);
                globalLayout.getChildren().set(2, mainContent);
                globalLayout.getChildren().set(3, movieForm);
                break;
            case AddTicketForm:
                stage.setTitle("Fabulous Cinema | Purchase tickets");
                title = new Title("Purchase tickets");
                mainContent = new MainContent(this, movieTheater, ComponentEnum.AddTicketForm);
                globalLayout.getChildren().set(1, title);
                globalLayout.getChildren().set(2, mainContent);
                globalLayout.getChildren().set(3, placeholder);
                break;
            case ManageMovies:
                stage.setTitle("Fabulous Cinema | Manage movies");
                title = new Title("Manage movies");
                manageMovies = new ManageMovies(this, movieTheater);
                globalLayout.getChildren().set(1, title);
                globalLayout.getChildren().set(2, manageMovies);
                globalLayout.getChildren().set(3, placeholder);
        }

    }

    public void addTicketForm(Showing showing) {
        addTicketForm = new AddTicketForm(movieTheater, showing, this);
       globalLayout.getChildren().set(3, addTicketForm);
    }

    public void updateMovieTheater(MovieTheater movieTheater, ComponentEnum componentEnum) {
        this.movieTheater = movieTheater;
        changeComponents(componentEnum);
    }

    public void closeForm() {
        stage.close();
    }

}
