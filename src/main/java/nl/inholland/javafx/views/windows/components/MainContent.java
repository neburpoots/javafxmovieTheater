package nl.inholland.javafx.views.windows.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.inholland.javafx.models.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import nl.inholland.javafx.views.windows.MainWindow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainContent extends HBox {
    private MovieTheater movieTheater;
    private MainWindow mainWindow;

    private List<Room> rooms;

    private TableView<Movie> roomOneTableView;
    private TableView<Movie> roomTwoTableView;
    private VBox roomOne;
    private VBox roomTwo;
    private ComponentEnum componentSender;


    public MainContent(MainWindow mainWindow, MovieTheater movieTheater, ComponentEnum componentSender) {
        this.movieTheater = movieTheater;
        this.mainWindow = mainWindow;
        this.setSpacing(20);
        this.setPadding(new Insets(10,10,10,10));
        this.getStyleClass().add("mainContent");
        this.componentSender = componentSender;

        rooms = movieTheater.getRooms();

        makeVBoxForTables();
    }

    private void makeVBoxForTables() {
        //room 1
        roomOne = new VBox();
        Label titleRoomOne = new Label("Room 1");
        //room 2
        roomTwo = new VBox();
        Label titleRoomTwo = new Label("Room 2");


        // Setting up the first room movie table view
        ObservableList<Showing> showingsForRoomOne = FXCollections.observableArrayList(rooms.get(0).getShowings());
        roomOneTableView = setupTables(showingsForRoomOne);

        // Setting up the second room movie table view
        ObservableList<Showing> showingsForRoomTwo = FXCollections.observableArrayList(rooms.get(1).getShowings());
        roomTwoTableView = setupTables(showingsForRoomTwo);

        roomOne.getChildren().addAll(titleRoomOne, roomOneTableView);
        roomTwo.getChildren().addAll(titleRoomTwo, roomTwoTableView);

        if(componentSender == ComponentEnum.AddTicketForm) {
            handleTableSelection();
        }

        this.getChildren().addAll(roomOne, roomTwo);
    }
    private void handleTableSelection() {
        roomOneTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(roomOneTableView.getSelectionModel().getSelectedItem() != null)
                {
                    Showing selectedShowing = (Showing)newValue;
                    addTicketform(selectedShowing);
                }
            }
        });

        roomTwoTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(roomTwoTableView.getSelectionModel().getSelectedItem() != null)
                {
                    Showing selectedShowing = (Showing)newValue;
                    addTicketform(selectedShowing);
                }
            }
        });
    }

    private void addTicketform(Showing selectedShowing) {
        mainWindow.addTicketForm(selectedShowing);
    }

    private TableView setupTables(ObservableList<Showing> showings) {
        TableView inputTableView = new TableView();

        inputTableView.getStyleClass().add("table");

        if(componentSender == ComponentEnum.AddTicketForm) {
            inputTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            inputTableView.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    inputTableView.getSelectionModel().clearSelection();
                }
            });
        } else if(componentSender == ComponentEnum.AddShowingForm) {
            inputTableView.setSelectionModel(null);
        }
        inputTableView.setEditable(true);


        TableColumn startCol = new TableColumn("Start");
        startCol.setMinWidth(160);
        startCol.setCellValueFactory(new PropertyValueFactory<Showing, LocalDateTime>("start"));

        TableColumn endCol = new TableColumn("End");
        endCol.setMinWidth(160);
        endCol.setCellValueFactory(new PropertyValueFactory<Showing, LocalDateTime>("end"));

        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(170);
        titleCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("title"));

        TableColumn seatsCol = new TableColumn("Seats");
        seatsCol.setMinWidth(80);
        seatsCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("totalTickets"));


        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<Showing, String>("price"));

        inputTableView.getColumns().addAll(startCol, endCol, titleCol, seatsCol, priceCol);

        inputTableView.setItems(showings);

        return inputTableView;
    }
}
