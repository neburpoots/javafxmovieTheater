package nl.inholland.javafx.views.windows.components.forms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.inholland.javafx.models.*;
import nl.inholland.javafx.views.windows.MainWindow;
import nl.inholland.javafx.views.windows.components.pdftemplates.TicketsForShowing;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddTicketForm extends HBox {
    private MovieTheater movieTheater;
    private MainWindow mainWindow;

    private Showing currentShowing;
    private GridPane gridPane;

    // Error message for time error
    private String timeError;

    //Nodes that need to be updated according to changes
    private ComboBox<Integer> seatsComboBox;
    private TextField nameTextField;
    private Label validationError;
    private CheckBox printCheck;

    public AddTicketForm(MovieTheater movieTheater, Showing showing, MainWindow mainWindow) {
        this.currentShowing = showing;
        this.mainWindow = mainWindow;
        this.movieTheater = movieTheater;
        this.getStyleClass().add("Form");

        this.getChildren().add(MakeForm());
    }

    public void updateTicketForm(MovieTheater movieTheater, Showing showing, MainWindow mainWindow) {
        this.currentShowing = showing;
        this.mainWindow = mainWindow;
        this.movieTheater = movieTheater;
        this.getStyleClass().add("Form");

        this.getChildren().remove(gridPane);
        this.getChildren().add(MakeForm());
    }

    private GridPane MakeForm() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setHgap(50);
        gridPane.setVgap(30);

        makeFirstFormRow();
        makeSecondFormRow();
        makeButtons();

        return gridPane;
    }

    private void makeButtons() {

        printCheck = new CheckBox("Print tickets");
        gridPane.add(printCheck, 4, 0);

        Button purchaseBTN  = new Button("Purchase");
        purchaseBTN.setMinWidth(150);
        gridPane.add(purchaseBTN, 4, 1);

        Button clearBTN  = new Button("Clear");
        clearBTN.setMinWidth(150);
        gridPane.add(clearBTN, 4, 2);

        //Adding error validation
        validationError = new Label();
        gridPane.add(validationError, 5, 0);

        //Events
        purchaseBTN.setOnAction(setButtonHandler());
        clearBTN.setOnAction(setClearButton());
    }

    private void addTickets() {
        boolean update = false;
        String test = "";
        this.getStyleClass().clear();
        this.getStyleClass().add("Form");
        if(nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()) {
            this.getStyleClass().add("wrongInput");
            validationError.setText("There is no name.");
        } else {
            for(Showing s: movieTheater.getRooms().get(currentShowing.getRoom().getId() - 1).getShowings()) {
                if(s.getShowingId().equals(currentShowing.getShowingId())) {
                    if(s.availableTickets() >= seatsComboBox.getValue()) {
                        update = true;
                        s.addTickets(nameTextField.getText(), seatsComboBox.getValue());
                        if(printCheck.isSelected()) {
                            setPDFHandler(nameTextField.getText(), seatsComboBox.getValue());
                        }
                    } else {
                        this.getStyleClass().add("wrongInput");
                        validationError.setText("Not enough tickets left.");
                    }
                }
            }

            if(update) {
               mainWindow.updateMovieTheater(movieTheater, ComponentEnum.AddTicketForm);
            }
        }
    }

    private List<Integer> numberOfSeats() {
        List<Integer> output = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            output.add(i);
        }
        return output;
    }

    private void makeSecondFormRow() {
        Label movieLBL = new Label("Movie title");
        Label noOfSeatsLBL = new Label("No. of seats");
        Label nameLBL = new Label("Name");
        Label movieValueLBL = new Label(currentShowing.getTitle());
        gridPane.add(movieLBL, 2, 0);
        gridPane.add(noOfSeatsLBL, 2, 1);
        gridPane.add(nameLBL, 2, 2);
        gridPane.add(movieValueLBL, 3, 0);


        //Sets the seats for combobox
        ObservableList<Integer> allSeats = FXCollections.observableArrayList(numberOfSeats());
        seatsComboBox = new ComboBox(allSeats);
        seatsComboBox.getSelectionModel().selectFirst();
        gridPane.add(seatsComboBox, 3, 1);

        nameTextField = new TextField();
        nameTextField.setMaxWidth(200);
        gridPane.add(nameTextField, 3, 2);

    }

    private void makeFirstFormRow() {
        Label roomLBL = new Label("Room");
        Label startTimeLBL = new Label("Start");
        Label endTimeLBL = new Label("End");

        gridPane.add(roomLBL, 0, 0);
        gridPane.add(startTimeLBL, 0, 1);
        gridPane.add(endTimeLBL, 0, 2);

        String roomId = Integer.toString(currentShowing.getRoom().getId());
        Label roomValueLBL = new Label("Room " + roomId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Label startTimeValueLBL = new Label(currentShowing.getStart().format(formatter));
        Label endTimeValueLBL = new Label(currentShowing.getEnd().format(formatter));

        gridPane.add(roomValueLBL, 1, 0);
        gridPane.add(startTimeValueLBL, 1, 1);
        gridPane.add(endTimeValueLBL, 1, 2);
    }

    private EventHandler setButtonHandler() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addTickets();
            }
        };
        return buttonHandler;
    }

    private EventHandler setClearButton() {
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainWindow.changeComponents(ComponentEnum.AddTicketForm);
            }
        };
        return buttonHandler;
    }

    private void setPDFHandler(String ticketname, int amount) {
       TicketsForShowing showingToPrint = new TicketsForShowing(ticketname, amount, currentShowing);
    }
}
