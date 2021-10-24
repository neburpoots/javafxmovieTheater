package nl.inholland.javafx.views.windows.components.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.inholland.javafx.models.ComponentEnum;
import nl.inholland.javafx.models.Movie;
import nl.inholland.javafx.models.MovieTheater;
import nl.inholland.javafx.views.windows.MainWindow;

public class AddMovieForm extends VBox {
    private MovieTheater movieTheater;
    private MainWindow mainWindow;
    private GridPane gridPane;

    private TextField titleTextField;
    private TextField priceEuroTextField;
    private TextField priceCentTextField;

    private TextField durationTextField;
    private Label validationError;

    public AddMovieForm(MovieTheater movieTheater, MainWindow mainWindow)
    {
        this.movieTheater = movieTheater;
        this.mainWindow = mainWindow;

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(50);
        gridPane.setVgap(30);
        gridPane.getStyleClass().add("Form");

        MakeForm();
        MakeButtons();

        //Adding error validation
        validationError = new Label();
        validationError.setPadding(new Insets(20, 20, 20, 20));
        this.getChildren().addAll(gridPane, validationError);
    }

    private void MakeForm() {
        Label movieLBL = new Label("Movie title");
        Label priceLBL = new Label("Price (Euro's, Cents)");
        Label durationLBL = new Label("Duration (in minutes)");
        gridPane.add(movieLBL, 0, 0);
        gridPane.add(priceLBL, 0, 1);
        gridPane.add(durationLBL, 0, 2);

        titleTextField = new TextField();
        titleTextField.setMaxWidth(200);
        gridPane.add(titleTextField, 1, 0);

        priceEuroTextField = new TextField();
        priceEuroTextField.setMaxWidth(80);
        priceEuroTextField.setPromptText("Euro's");

        Label dotLabel = new Label("  .  ");

        priceCentTextField = new TextField();
        priceCentTextField.setMaxWidth(80);
        priceCentTextField.setPromptText("Cents");

        GridPane priceGridPane = new GridPane();
        priceGridPane.add(priceEuroTextField, 0, 0);
        priceGridPane.add(dotLabel, 1, 0);
        priceGridPane.add(priceCentTextField, 2, 0);

        gridPane.add(priceGridPane, 1, 1);

        durationTextField = new TextField();
        durationTextField.setMaxWidth(200);
        gridPane.add(durationTextField, 1, 2);
    }

    private void MakeButtons() {
        Button addMovieBTN  = new Button("Add movie");
        addMovieBTN.setMinWidth(150);
        gridPane.add(addMovieBTN, 0, 3);

        Button clearBTN  = new Button("Clear");
        clearBTN.setMinWidth(150);
        gridPane.add(clearBTN, 1, 3);

        //Events
        addMovieBTN.setOnAction(setButtonHandler());
        clearBTN.setOnAction(setClearButton());
    }

    private void AddmovieToTheater() {
        gridPane.getStyleClass().clear();
        gridPane.getStyleClass().add("Form");
        try {
            if(CheckIfNull(titleTextField)) {
             throw new Exception("Title is empty");
            } else if(CheckIfNull(priceEuroTextField) && CheckIfNull(priceCentTextField)) {
                throw new Exception("Price is empty");
            } else if(CheckIfNull(durationTextField)) {
                throw new Exception("Duration is empty");
            } else if(Integer.parseInt(durationTextField.getText()) < 1) {
                throw new Exception("Duration can't be under null");
            }

            String title = titleTextField.getText();
            double price = checkPrice();
            int duration = Integer.parseInt(durationTextField.getText());

            movieTheater.addMovie(new Movie(title, price, duration));
            mainWindow.updateMovieTheater(movieTheater, ComponentEnum.ManageMovies);
            validationError.setText("Succesfully added movie.");
        }  catch(NumberFormatException err) {
            this.getStyleClass().add("wrongInput");
            validationError.setText("One of the inputs does not have a valid number");
        } catch(Exception err) {
            this.getStyleClass().add("wrongInput");
            validationError.setText(err.getMessage());
        }
    }

    private double checkPrice() throws Exception
    {
        int priceEuro = Integer.parseInt(priceEuroTextField.getText());
        int priceCent = Integer.parseInt(priceCentTextField.getText());

        if(priceEuro > 0 && priceCent >= 0 || priceEuro >= 0 && priceCent > 0) {
            if(priceCent < 100) {
                String fullPrice = priceEuroTextField.getText() + "." + priceCentTextField.getText();
                return Double.parseDouble(fullPrice);
            } else {
                throw new Exception("Cent is not valid");
            }
        } else {
            throw new Exception("Price not valid");
        }
    }

    private boolean CheckIfNull(TextField checkField) {
        if(checkField.getText() == null || checkField.getText().trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    private EventHandler setButtonHandler() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddmovieToTheater();
            }
        };
        return buttonHandler;
    }

    private EventHandler setClearButton() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MakeForm();
            }
        };
        return buttonHandler;
    }
}
