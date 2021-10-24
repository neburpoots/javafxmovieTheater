package nl.inholland.javafx.views.windows.components.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import nl.inholland.javafx.models.*;
import nl.inholland.javafx.views.windows.MainWindow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddShowingForm extends HBox {
    private MovieTheater movieTheater;
    private MainWindow mainWindow;

    private Showing currentShowing;
    private GridPane gridPane;

    // Error message for time error
    private String timeError;

    //Nodes that need to be updated according to changes
    private ComboBox<Movie> movieComboBox;
    private DatePicker startDatePicker;
    private TextField timeHour;
    private TextField timeMin;
    private Label endTime;
    private Label priceLBL;
    private Label validationError;

    public AddShowingForm(MovieTheater movieTheater, MainWindow mainWindow) {
        this.movieTheater = movieTheater;
        this.mainWindow = mainWindow;
        this.getStyleClass().add("Form");

        this.timeError = "No end time available";

        Room setRoom = movieTheater.getRooms().get(0);
        this.currentShowing = new Showing(setRoom, movieTheater.getAllMovies().get(0), LocalDateTime.now());

        this.getChildren().add(makeForm());
    }

    private GridPane makeForm() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(50);
        gridPane.setVgap(30);

        makeFirstFormRow();
        makeSecondFormRow();
        makeButtons();

        return gridPane;
    }

    private void updateEndTime() {
        try
        {
            String datetoConvert = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String timeToConvert = "";

            if(timeHour.getText().length() == 1 && timeMin.getText().length() == 1) {
                timeToConvert = "0" + timeHour.getText() + ":" + "0" + timeMin.getText();
            } else if(timeHour.getText().length() == 1){
                timeToConvert = "0" + timeHour.getText() + ":" + timeMin.getText();
            } else if(timeMin.getText().length() == 1) {
                timeToConvert = timeHour.getText() + ":" + "0" +  timeMin.getText();
            } else {
                timeToConvert = timeHour.getText() + ":" + timeMin.getText();
            }

            String stringToConvert = datetoConvert + " " + timeToConvert;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            currentShowing.setStart(LocalDateTime.parse(stringToConvert, formatter));

            String formattedTime = currentShowing.getEnd().format(formatter);

            endTime.setText(formattedTime);
        } catch (Exception e) {
            endTime.setText(timeError);
        }
    }

    private void updatePrice() {
        priceLBL.setText(String.valueOf(currentShowing.getMovie().getPrice()));
    }

    private void makeButtons() {
        Button addShowingBTN  = new Button("Add showing");
        addShowingBTN.setMinWidth(150);
        gridPane.add(addShowingBTN, 4, 1);

        Button clearBTN  = new Button("Clear");
        clearBTN.setMinWidth(150);
        gridPane.add(clearBTN, 4, 2);

        //Adding error validation
        validationError = new Label();
        gridPane.add(validationError, 5, 0);

        //Events
        addShowingBTN.setOnAction(setButtonHandler());
        clearBTN.setOnAction(setClearButton());

    }

    private void addShowing() {
        this.getStyleClass().clear();
        this.getStyleClass().add("Form");
        int roomId = currentShowing.getRoom().getId();
        if(endTime.getText() == "No end time available") {
            this.getStyleClass().add("wrongInput");
            validationError.setText("Something is wrong with the start time.");
        } else if (!movieTheater.getRooms().get(roomId - 1).checkShowingTime(currentShowing)) {
            this.getStyleClass().add("wrongInput");
            validationError.setText( "This time conflicts with another \n movie being played in this room. ");
        } else {
            movieTheater.getRooms().get(roomId - 1).setShowing(currentShowing);
            refreshRoomTables();
            validationError.setText("Succesfully added showing.");
        }
    }

    private void refreshRoomTables() {
        mainWindow.updateMovieTheater(movieTheater, ComponentEnum.AddShowingForm);
    }

    private void makeSecondFormRow() {
        Label StartLBL = new Label("Start");
        Label EndLBL = new Label("End");
        Label PriceLBL = new Label("Price");
        gridPane.add(StartLBL, 2, 0);
        gridPane.add(EndLBL, 2, 1);
        gridPane.add(PriceLBL, 2, 2);

        startDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());
        gridPane.add(startDatePicker, 3, 0);

        HBox timeHBox = new HBox();
        timeHour = new TextField("20");
        timeHour.setMaxWidth(70);
        Label betweenTime = new Label(" : ");
        timeMin = new TextField("00");
        timeMin.setMaxWidth(70);
        timeHBox.getChildren().addAll(timeHour, betweenTime, timeMin);
        gridPane.add(timeHBox, 4, 0);

        //Sets and updates end time
        endTime = new Label();
        updateEndTime();
        gridPane.add(endTime, 3, 1);

        //Sets and updates the price
        priceLBL = new Label();
        updatePrice();
        gridPane.add(priceLBL, 3, 2);

        //All events are placed here
        hourTextBoxChange();
        minTextBoxChange();
        datePickerChange();
    }

    private void makeFirstFormRow() {
        Label movieTitleLBL = new Label("Movie title");
        Label roomLBL = new Label("Room");
        Label numSeatsLBL = new Label("No. of seats");
        gridPane.add(movieTitleLBL, 0, 0);
        gridPane.add(roomLBL, 0, 1);
        gridPane.add(numSeatsLBL, 0, 2);

        //Gets all movies for combobox
        ObservableList<Movie> allMovies = FXCollections.observableArrayList(movieTheater.getAllMovies());
        movieComboBox = new ComboBox(allMovies);
        movieComboBox.getSelectionModel().selectFirst();

        //Uses a override method to display title and not the object
        convertMovieComboDisplayList(movieComboBox);
        movieComboBox.itemsProperty().setValue(allMovies);
        gridPane.add(movieComboBox, 1, 0);

        //Gets all the rooms
        ObservableList<Room> allRooms = FXCollections.observableArrayList(movieTheater.getRooms());
        ComboBox<Room> roomComboBox = new ComboBox(allRooms);
        roomComboBox.getSelectionModel().selectFirst();

        //Uses a override method to display title and not the object
        convertRoomComboDisplayList(roomComboBox);
        roomComboBox.itemsProperty().setValue(allRooms);
        gridPane.add(roomComboBox, 1, 1);

        Label numSeatsLBLValue = new Label("200");
        gridPane.add(numSeatsLBLValue, 1, 2);

        //All events are placed here
        roomComboBoxChange(roomComboBox, numSeatsLBLValue);
        movieComboBoxChange();
    }


    private void roomComboBoxChange(ComboBox roomComboBox, Label numSeatsLBLValue) {
        roomComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Room>() {
            @Override
            public void changed(ObservableValue<? extends Room> observableValue,
                                Room oldRoom, Room newRoom) {
                currentShowing.setRoom(newRoom);
                numSeatsLBLValue.setText(Integer.toString(newRoom.getSeats()));
            }
        });
    }

    private void convertRoomComboDisplayList(ComboBox<Room> movieComboBox) {
        movieComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                return "Room" + room.getId();
            }

            @Override
            public Room fromString(final String string) {
                return movieComboBox.getItems().stream().filter(movie -> movie.getIdString().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void convertMovieComboDisplayList(ComboBox<Movie> movieComboBox) {
        movieComboBox.setConverter(new StringConverter<Movie>() {
            @Override
            public String toString(Movie movie) {
                return movie.getTitle();
            }
            @Override
            public Movie fromString(final String string) {

                Movie comboactiveMovie = movieComboBox.getItems().stream().filter(movie -> movie.getTitle().equals(string)).findFirst().orElse(null);
                currentShowing.setMovie(comboactiveMovie);
                updateEndTime();
                return comboactiveMovie;
            }
        });
    }

    private void movieComboBoxChange() {
        movieComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>() {
            @Override
            public void changed(ObservableValue<? extends Movie> observableValue,
                                Movie oldMovie, Movie newMovie) {
                currentShowing.setMovie(newMovie);
                updateEndTime();
                updatePrice();
            }
        });
    }

    private void hourTextBoxChange() {
        timeHour.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(newValue != "") {
                        int hourTime = Integer.parseInt(newValue);
                        if(hourTime >= 0 && hourTime < 24 ) {
                            updateEndTime();
                        } else {
                            throw new Exception();
                        }
                    }
                } catch (Exception e) {
                    endTime.setText(timeError);
                }
            }
        });
    }

    private void minTextBoxChange() {
        timeMin.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(newValue != "") {
                        int minTime = Integer.parseInt(newValue);
                        if(minTime >= 0 && minTime < 60 ) {
                            updateEndTime();
                        } else {
                            throw new Exception();
                        }
                    }
                } catch (Exception e) {
                    endTime.setText(timeError);
                }
            }
        });
    }

    private void datePickerChange() {
        startDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>(){
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                updateEndTime();
            }
        });
    }

    private EventHandler setButtonHandler() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addShowing();
            }
        };
        return buttonHandler;
    }

    private EventHandler setClearButton() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.changeComponents(ComponentEnum.AddShowingForm);
            }
        };
        return buttonHandler;
    }
}
