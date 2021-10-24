package nl.inholland.javafx.views.windows.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.inholland.javafx.models.*;
import nl.inholland.javafx.models.csv.CSVParser;
import nl.inholland.javafx.views.windows.MainWindow;
import nl.inholland.javafx.views.windows.components.forms.AddMovieForm;

import java.util.List;


public class ManageMovies extends HBox {
    private MainWindow mainWindow;
    private MovieTheater movieTheater;

    private TableView<Movie> movieTable;
    private GridPane gridPane;

    public ManageMovies(MainWindow mainWindow, MovieTheater movieTheater) {
        this.mainWindow = mainWindow;
        this.movieTheater = movieTheater;
        gridPane = new GridPane();
        gridPane.getStyleClass().add("Form");
        gridPane.setHgap(50);
        gridPane.setVgap(30);
        this.setSpacing(20);
        this.setPadding(new Insets(10,10,10,10));



        // Setting up the movies table
        ObservableList<Movie> allMovies = FXCollections.observableArrayList(movieTheater.getAllMovies());
        movieTable = setupTable(allMovies);

        gridPane.add(movieTable, 0,0);
        this.getChildren().addAll(gridPane);
        MakeButtons();
    }

    private TableView setupTable(ObservableList<Movie> allMovies) {
        TableView inputTableView = new TableView();

        inputTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(300);
        titleCol.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));

        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(200);
        priceCol.setCellValueFactory(new PropertyValueFactory<Movie, Double>("price"));

        TableColumn durationCol = new TableColumn("Duration");
        durationCol.setMinWidth(200);
        durationCol.setCellValueFactory(new PropertyValueFactory<Showing, Integer>("duration"));


        inputTableView.getColumns().addAll(titleCol, priceCol, durationCol);

        inputTableView.setItems(allMovies);

        return inputTableView;
    }

    private void MakeButtons() {
        Button addMovieBTN  = new Button("Add a new movie");
        addMovieBTN.setMinWidth(150);
        gridPane.add(addMovieBTN, 0, 1);

        //events
        addMovieBTN.setOnAction(setButtonHandler());

    }

    private EventHandler setButtonHandler() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddMovieForm addMovieForm = new AddMovieForm(movieTheater, mainWindow);
                gridPane.add(addMovieForm, 1, 0);
            }
        };
        return buttonHandler;
    }
}
