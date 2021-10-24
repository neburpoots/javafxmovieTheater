package nl.inholland.javafx.views;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class StyledScene extends Scene {

    public StyledScene(Parent parent) {
        super(parent);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(this);
        this.getStylesheets().add("css/style.css");
    }
}
