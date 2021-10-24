package nl.inholland.javafx.views.windows.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Title extends HBox {
    public Title(String title) {
        //label header
        this.setSpacing(20);
        this.setPadding(new Insets(10,10,10,10));
        Label ticketsHeader = new Label(title);
        ticketsHeader.getStyleClass().add("header");
        this.getStyleClass().add("titleHBox");
        this.getChildren().addAll(ticketsHeader);
    }
}
